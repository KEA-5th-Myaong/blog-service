package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.Prefix;
import myaong.popolog.blogservice.dto.request.PostCreateRequest;
import myaong.popolog.blogservice.dto.request.PostUpdateRequest;
import myaong.popolog.blogservice.dto.response.*;
import myaong.popolog.blogservice.entity.Bookmark;
import myaong.popolog.blogservice.entity.Like;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.feign.constant.NotificationType;
import myaong.popolog.blogservice.feign.service.NotificationFeignService;
import myaong.popolog.blogservice.repository.BookmarkRepository;
import myaong.popolog.blogservice.repository.LikeRepository;
import myaong.popolog.blogservice.repository.PostRepository;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.micrometer.common.util.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsServiceImpl implements PostsService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final NotificationFeignService notificationFeignService;
    private final ProfileRepository profileRepository;
    private final S3ApiService s3ApiService;

    @Override
    public PostDetailResponse getPostDetails(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));
        return buildPostDetailResponse(post, memberId);
    }

    @Override
    public PostDetailResponse getPostByUrl(String username, String title, Long memberId) {
        Post post = postRepository.findByUsernameAndTitle(username, title)
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));
        return buildPostDetailResponse(post, memberId);
    }

    private PostDetailResponse buildPostDetailResponse(Post post, Long memberId) {
        List<PostDetailResponse.Comment> commentResponses = post.getComments().stream()
                .map(comment -> PostDetailResponse.Comment.builder()
                        .profilePicUrl(comment.getProfile() != null ? comment.getProfile().getProfilePicUrl() : null)
                        .memberId(comment.getProfile() != null ? comment.getProfile().getId() : null)
                        .nickname(comment.getProfile() != null ? comment.getProfile().getNickname() : null)
                        .commentId(comment.getId())
                        .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                        .comment(comment.getContent())
                        .timestamp(comment.getCreatedAt())
                        .build())
                .toList();

        boolean isBookmarked = memberId != null && bookmarkRepository.existsByPostAndMemberId(post, memberId);
        boolean isLiked = memberId != null && likeRepository.existsByPostAndMemberId(post, memberId);

        return PostDetailResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .timestamp(post.getCreatedAt())
                .memberId(post.getProfile() != null ? post.getProfile().getId() : null)
                .username(post.getProfile() != null ? post.getProfile().getUsername() : null)
                .nickname(post.getProfile() != null ? post.getProfile().getNickname() : null)
                .profilePicUrl(post.getProfile() != null ? post.getProfile().getProfilePicUrl() : null)
                .likeCount(post.getLikes().size())
                .isLiked(isLiked)
                .isBookmarked(isBookmarked)
                .commentCount(post.getComments().size())
                .comments(commentResponses)
                .build();
    }

    @Override
    public PostsResponse getPostsOf(Long memberId, Long lastId) {
        // lastId 검증
        validateLastId(lastId);

        // 회원 프로필 검증
        Profile profile = profileRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND));

        // 게시물 조회 (자신의 게시물만)
        List<Post> postList = postRepository.findTop10ByProfile_IdAndIdLessThanOrderByIdDesc(memberId, lastId);

        // 응답 데이터 준비
        List<PostsResponse.Posts> posts = new ArrayList<>();
        long minId = Long.MAX_VALUE;
        for (Post p : postList) {
            Long postId = p.getId();
            if (postId.compareTo(minId) < 0) {
                minId = postId;
            }

            boolean isBookmarked = bookmarkRepository.existsByPostAndMemberId(p, memberId);

            PostsResponse.Posts post = PostsResponse.Posts.builder()
                    .postId(postId)
                    .title(p.getTitle())
                    .content(truncateContent(p.getContent()))
                    .timestamp(p.getCreatedAt())
                    .likeCount(p.getLikes().size())
                    .isBookmarked(isBookmarked)
                    .build();

            posts.add(post);
        }

        // 데이터가 더 이상 없는 경우 lastId를 -1로 설정
        if (postList.size() < 10) {
            minId = -1L;
        }

        return PostsResponse.builder()
                .lastId(minId)
                .posts(posts)
                .build();
    }

    @Override
    public PostCreateResponse createPost(Long memberId, PostCreateRequest request) {
        // 작성자 프로필 검증
        Profile profile = profileRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND));

        // 이미지 URL 이동 처리 (임시 저장소 -> 영구 저장소)
        if (request.getPicUrl() != null && !request.getPicUrl().isEmpty()) {
            String persistentPicUrl = s3ApiService.moveToPersistentStorage(request.getPicUrl());
            request.setPicUrl(persistentPicUrl); // URL 업데이트
        }

        // 게시물 저장
        Post post = postRepository.save(Post.builder()
                .profile(profile)
                .title(request.getTitle())
                .content(request.getContent())
                .isBlinded(false)
                .build());

        // 응답 반환
        return PostCreateResponse.builder()
                .postId(post.getId())
                .build();
    }

    @Override
    public void updatePost(Long postId, Long memberId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));

        if (!post.getProfile().getId().equals(memberId)) {
            throw new ApiException(ApiCode.READ_ONLY_ACCESS_POST);
        }

        // 제목 및 내용 공백 여부 확인
        if (isBlank(request.getTitle()) || isBlank(request.getContent())) {
            throw new ApiException(ApiCode.INVALID_DATA);
        }

        if (request.getTitle() != null) post.updateTitle(request.getTitle());
        if (request.getContent() != null) post.updateContent(request.getContent());

        postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId, Long memberId) {
        // 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));

        // 작성자 확인
        if (!post.getProfile().getId().equals(memberId)) {
            throw new ApiException(ApiCode.READ_ONLY_ACCESS_POST);
        }

        // 게시물 삭제
        postRepository.delete(post);
    }

    @Override
    public PostPicResponse uploadPostImage(Long memberId, MultipartFile image) {
        // 이미지 업로드 처리 (S3 업로드)
        String imageUrl = s3ApiService.uploadToTempStorage(Prefix.POST, image);
        // 응답 객체 생성 및 반환
        return PostPicResponse.builder()
                .picUrl(imageUrl)
                .build();
    }


    @Override
    public LikeResponse toggleLike(Long postId, Long memberId) {
        // 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));

        // 기존 좋아요 여부 확인
        Optional<Like> existingLike = likeRepository.findByPostIdAndMemberId(postId, memberId);

        boolean isLiked;

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            isLiked = false;
        } else {
            Like like = Like.builder()
                    .post(post)
                    .memberId(memberId)
                    .build();
            likeRepository.save(like);
            isLiked = true;

            sendLikeNotification(post, memberId);
        }

        return LikeResponse.builder()
                .like(isLiked)
                .build();
    }

    private void sendLikeNotification(Post post, Long memberId) {
        // 좋아요를 누른 사용자의 닉네임 가져오기
        String likerNickname = profileRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND))
                .getNickname(); // 닉네임 가져오기

        // 알림 제목 생성
        String title = String.format("%s님이 회원님의 게시물을 좋아합니다!", likerNickname);
        String url = "/posts/" + post.getId(); // 게시물 URL
        Long targetMemberId = post.getProfile().getId();

        // 알림 전송
        notificationFeignService.sendNotification(targetMemberId, title, "", url, NotificationType.LIKE, memberId);
    }

    private void validateLastId(Long lastId) {
        if (lastId < 0) {
            throw new ApiException(ApiCode.INVALID_DATA);
        }
    }

    private void validateProfileExists(Long memberId) {
        boolean exists = profileRepository.existsById(memberId);
        if (!exists) {
            throw new ApiException(ApiCode.MEMBER_NOT_FOUND);
        }
    }

    private String truncateContent(String content) {
        return content.length() > 400 ? content.substring(0, 400) : content;
    }

    @Override
    public PostBookmarkResponse toggleBookmark(Long postId, Long memberId) {
        // 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));

        // 회원 프로필 조회
        Profile profile = profileRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND));

        // 북마크 여부 확인
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByPostAndProfile(post, profile);

        boolean isBookmarked;

        if (existingBookmark.isPresent()) {
            // 이미 북마크가 존재하면 삭제
            bookmarkRepository.delete(existingBookmark.get());
            isBookmarked = false;
        } else {
            // 북마크가 없으면 생성
            Bookmark bookmark = Bookmark.builder()
                    .profile(profile)
                    .post(post)
                    .build();
            bookmarkRepository.save(bookmark);
            isBookmarked = true;
        }

        return PostBookmarkResponse.builder()
                .bookmark(isBookmarked)
                .build();
    }

}
