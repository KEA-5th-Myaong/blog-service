package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.Prefix;
import myaong.popolog.blogservice.dto.request.PostCreateRequest;
import myaong.popolog.blogservice.dto.request.ReportRequest;
import myaong.popolog.blogservice.dto.response.*;
import myaong.popolog.blogservice.entity.*;
import myaong.popolog.blogservice.enums.ContentsType;
import myaong.popolog.blogservice.feign.constant.NotificationType;
import myaong.popolog.blogservice.feign.service.NotificationFeignService;
import myaong.popolog.blogservice.repository.*;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class PostsServiceImpl implements PostsService {

    private final ProfileQueryService profileQueryService;
    private final S3ApiService s3ApiService;
    private final NotificationFeignService notificationFeignService;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;

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
    public PostsResponse getPostsOf(Long requesterId, Long memberId, Long lastId) {
        // lastId 검증
        validateLastId(lastId);

        // 회원 프로필 검증
        Profile profile = profileQueryService.findById(memberId);

        // 게시물 조회
        List<Post> postList;
        if (lastId == 0) {
            // lastId가 0일 경우, 최근 게시물부터 조회
            postList = postRepository.findTop10ByProfileOrderByIdDesc(profile);
        } else {
            // lastId가 0이 아닐 경우, 해당 ID보다 작은 게시물 조회
            postList = postRepository.findTop10ByProfileAndIdLessThanOrderByIdDesc(profile, lastId);
        }

        // 응답 데이터 준비
        List<PostsResponse.Posts> posts = new ArrayList<>();
        long minId = Long.MAX_VALUE;
        for (Post p : postList) {
            Long postId = p.getId();
            if (postId.compareTo(minId) < 0) {
                minId = postId;
            }

            boolean isBookmarked = requesterId != null && bookmarkRepository.existsByPostAndMemberId(p, requesterId);

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
        Profile profile = profileQueryService.findById(memberId);

        String newContent = processImgUrl(request.getContent(), s3ApiService::moveToPersistentStorage);

        // 게시물 저장
        Post post = postRepository.save(Post.builder()
                .profile(profile)
                .title(request.getTitle())
                .content(newContent)
                .isBlinded(false)
                .build());

        // 응답 반환
        return PostCreateResponse.builder()
                .postId(post.getId())
                .build();
    }

    @Override
    public void updatePost(Long postId, Long memberId, PostCreateRequest request) {

        Profile profile = profileQueryService.findById(memberId);
        Post post = validatePermissionAndGetPostById(profile, postId);

        // 기존 포스트의 이미지 이동
        processImgUrl(post.getContent(), s3ApiService::moveToTempStorage);

        // 새 포스트의 이미지 URL도 temp에 있는 것으로 변환
        String newContent = processImgUrl(request.getContent(), s3ApiService::replacePersistentToTemp);

        // 새 포스트 이미지 변환
        newContent = processImgUrl(newContent, s3ApiService::moveToPersistentStorage);

        post.updateTitle(request.getTitle());
        post.updateContent(newContent);

        postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId, Long memberId) {

        Profile profile = profileQueryService.findById(memberId);
        Post post = validatePermissionAndGetPostById(profile, postId);

        // 기존 포스트 이미지 삭제
        processImgUrl(post.getContent(), img -> {
            s3ApiService.deleteFromPersistentStorage(img);
            return null;
        });

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

    private Post validatePermissionAndGetPostById(Profile profile, Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));

        if (!post.getProfile().equals(profile)) {
            throw new ApiException(ApiCode.READ_ONLY_ACCESS_POST);
        }

        return post;
    }

    private String processImgUrl(String html, Function<String, String> function) {
        Document doc = Jsoup.parse(html);
        Elements imgUrls = doc.select("img");

        // img src 변환
        for (Element imgUrl : imgUrls) {
            imgUrl.attr("src", function.apply(imgUrl.attr("src")));
        }

        return doc.select("body").html();
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
        String likerNickname = profileQueryService.findById(memberId)
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

    private String truncateContent(String content) {
        return content.length() > 400 ? content.substring(0, 400) : content;
    }

    @Override
    public PostBookmarkResponse toggleBookmark(Long postId, Long memberId) {
        // 게시물 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));

        // 회원 프로필 조회
        Profile profile = profileQueryService.findById(memberId);

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

    @Override
    @Transactional
    public void reportPost(Long memberId, ReportRequest request) {
        // 콘텐츠 존재 여부 확인
        ContentsType contentsType = request.getContentType();

        if (contentsType == ContentsType.POST) {
            postRepository.findById(request.getContentId())
                    .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));
        } else if (contentsType == ContentsType.COMMENT) {
            commentRepository.findById(request.getContentId())
                    .orElseThrow(() -> new ApiException(ApiCode.COMMENT_NOT_FOUND));
        }

        // 중복 신고 여부 확인
        boolean isAlreadyReported = reportRepository.existsByProfileIdAndContentsIdAndContentsType(
                memberId, request.getContentId(), contentsType);

        if (isAlreadyReported) {
            throw new ApiException(ApiCode.REPORT_DUPLICATED);
        }

        // 신고한 회원 정보 확인
        Profile profile = profileQueryService.findById(memberId);

        // 신고 데이터 생성 및 저장
        Report report = Report.builder()
                .profile(profile)
                .contentsId(request.getContentId())
                .contentsType(contentsType)
                .build();

        reportRepository.save(report);
    }
}
