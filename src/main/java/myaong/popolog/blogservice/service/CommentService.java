package myaong.popolog.blogservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.dto.request.CommentPostRequest;
import myaong.popolog.blogservice.dto.request.CommentUpdateRequest;
import myaong.popolog.blogservice.dto.request.ReplyRequest;
import myaong.popolog.blogservice.dto.request.ReplyUpdateRequest;
import myaong.popolog.blogservice.dto.response.CommentPostResponse;
import myaong.popolog.blogservice.dto.response.CommentUpdateResponse;
import myaong.popolog.blogservice.dto.response.ReplyResponse;
import myaong.popolog.blogservice.entity.Comment;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.feign.constant.NotificationType;
import myaong.popolog.blogservice.feign.service.NotificationFeignService;
import myaong.popolog.blogservice.repository.CommentRepository;
import myaong.popolog.blogservice.repository.PostRepository;
import myaong.popolog.blogservice.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final NotificationFeignService notificationFeignService;

    // 댓글 작성
    @Transactional
    public CommentPostResponse postComment(Long memberId, CommentPostRequest request) {

        // 게시물 조회
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));

        // 댓글 작성자 프로필 조회
        Profile commenterProfile = profileRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND));
        // 댓글 저장
        Comment comment = Comment.builder()
                .post(post)
                .profile(commenterProfile) // 댓글 작성자 프로필 설정
                .content(request.getContent())
                .isBlinded(false)
                .build();

        commentRepository.save(comment);

        // 댓글 작성자가 게시물 작성자가 아닐 경우 알림 전송
        if (!post.getProfile().getId().equals(memberId)) {
            sendNotificationToPostOwner(post, null, commenterProfile.getNickname(), request.getContent(), memberId, NotificationType.COMMENT);
        }

        // 댓글 작성 결과 반환
        return CommentPostResponse.builder()
                .commentId(comment.getId())
                .build();
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ApiCode.COMMENT_NOT_FOUND));
        if (!comment.getProfile().getId().equals(memberId)) {
            throw new ApiException(ApiCode.READ_ONLY_ACCESS);
        }
        commentRepository.delete(comment);
    }

    // 답글 작성
    @Transactional
    public ReplyResponse postReply(Long memberId, ReplyRequest request) {
        Comment parentComment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new ApiException(ApiCode.COMMENT_NOT_FOUND));

        // 답글 작성자 프로필 조회
        Profile commenterProfile = profileRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND));
        if (request.getContent().trim().isEmpty()) {
            throw new ApiException(ApiCode.INVALID_DATA);
        }

        // 답글 엔티티 생성
        Comment reply = Comment.builder()
                .post(parentComment.getPost())
                .profile(commenterProfile)
                .parentComment(parentComment) // 부모 댓글 설정
                .content(request.getContent())
                .isBlinded(false)
                .build();

        commentRepository.save(reply);

        // 부모 댓글 작성자와 답글 작성자가 다를 경우 알림 전송
        if (!parentComment.getProfile().getId().equals(memberId)) {
            sendNotificationToCommentOwner(
                    parentComment.getPost(),
                    parentComment,
                    commenterProfile.getNickname(),
                    request.getContent(),
                    memberId
            );
        }

        // 게시물 작성자에게도 답글 알림 전송
        sendNotificationToPostOwner(
                parentComment.getPost(),
                parentComment,
                commenterProfile.getNickname(),
                request.getContent(),
                memberId,
                NotificationType.REPLY
        );

        // 답글 작성 결과 반환
        return ReplyResponse.builder()
                .commentId(reply.getId())
                .build();
    }


    // 답글 삭제
    @Transactional
    public void deleteReply(Long memberId, Long commentId) {
        Comment reply = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ApiCode.COMMENT_NOT_FOUND));

        if (!reply.getProfile().getId().equals(memberId)) {
            throw new ApiException(ApiCode.READ_ONLY_ACCESS);
        }

        if (reply.getParentComment() == null) {
            throw new ApiException(ApiCode.INVALID_DATA);
        }

        commentRepository.delete(reply);
    }

    // 수정 로직
    private Comment updateContent(Long memberId, Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ApiCode.COMMENT_NOT_FOUND));
        if (!comment.getProfile().getId().equals(memberId)) {
            throw new ApiException(ApiCode.READ_ONLY_ACCESS);
        }
        if (newContent == null || newContent.trim().isEmpty()) {
            throw new ApiException(ApiCode.INVALID_DATA);
        }
        comment.updateContent(newContent);
        commentRepository.save(comment);
        return comment;
    }

    // 댓글 수정
    @Transactional
    public CommentUpdateResponse updateComment(Long memberId, Long commentId, CommentUpdateRequest request) {
        Comment updatedComment = updateContent(memberId, commentId, request.getContent());
        return CommentUpdateResponse.builder()
                .commentId(updatedComment.getId())
                .build();
    }

    // 답글 수정
    @Transactional
    public ReplyResponse updateReply(Long memberId, Long replyId, ReplyUpdateRequest request) {
        Comment updatedReply = updateContent(memberId, replyId, request.getContent());
        return ReplyResponse.builder()
                .commentId(updatedReply.getId())
                .build();
    }

    private void sendNotificationToPostOwner(Post post, Comment parentComment, String commenterNickname, String content, Long memberId, NotificationType notificationType) {
        String title;

        // 알림 제목 생성
        if (notificationType == NotificationType.REPLY) {
            title = String.format("%s님이 %s님의 댓글에 답글을 남겼습니다.",
                    commenterNickname,
                    parentComment.getProfile().getNickname());
        } else {
            title = String.format("%s님이 게시물에 댓글을 남겼습니다.", commenterNickname);
        }

        // URL 생성
        String url = "/posts/" + post.getId();

        // 알림 전송
        notificationFeignService.sendNotification(post.getProfile().getId(), title, content, url, notificationType, memberId);
    }
    private void sendNotificationToCommentOwner(Post post, Comment parentComment, String commenterNickname, String replyContent, Long memberId) {
        String title = String.format("%s님이 댓글에 답글을 남겼습니다.", commenterNickname);
        String url = "/posts/" + post.getId();

        notificationFeignService.sendNotification(parentComment.getProfile().getId(), title, replyContent, url, NotificationType.REPLY, memberId);
    }
}


