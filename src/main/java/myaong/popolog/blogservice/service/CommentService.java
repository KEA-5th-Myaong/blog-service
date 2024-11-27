package myaong.popolog.blogservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.dto.request.CommentPostRequest;
import myaong.popolog.blogservice.dto.response.CommentPostResponse;
import myaong.popolog.blogservice.entity.Comment;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.feign.client.NotificationServiceFeignClient;
import myaong.popolog.blogservice.feign.constant.NotificationType;
import myaong.popolog.blogservice.feign.dto.request.NotificationRequest;
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
    private final NotificationServiceFeignClient notificationServiceFeignClient;

    @Transactional
    public CommentPostResponse postComment(Long memberId, CommentPostRequest request) {

        // 댓글 내용 검증
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new ApiException(ApiCode.INVALID_DATA);
        }

        // 게시물 조회
        Post post = postRepository.findById(Long.valueOf(request.getPostId()))
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
            sendNotificationToPostOwner(post, commenterProfile.getNickname(), request.getContent(), memberId);
        }

        // 댓글 작성 결과 반환
        return CommentPostResponse.builder()
                .commentId(comment.getId().intValue())
                .build();
    }

    // 알림 전송 메서드
    private void sendNotificationToPostOwner(Post post, String commenterNickname, String commentContent, Long memberId) {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .memberId(post.getProfile().getId())
                .title(String.format("%s님이 게시물에 댓글을 남겼습니다.", commenterNickname)) // 알림 제목
                .content(commentContent)
                .url("/posts/" + post.getId())
                .type(NotificationType.COMMENT)
                .build();

        // 알림 서비스로 전송
        notificationServiceFeignClient.sendNotification(notificationRequest, NotificationType.COMMENT.name(), memberId);
    }
}



