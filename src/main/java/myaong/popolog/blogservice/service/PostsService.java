package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.dto.response.LikeResponse;
import myaong.popolog.blogservice.dto.response.PostDetailResponse;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.entity.Comment;
import myaong.popolog.blogservice.entity.Like;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.feign.constant.NotificationType;
import myaong.popolog.blogservice.feign.service.NotificationFeignService;
import myaong.popolog.blogservice.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostsService {

	private final LikeRepository likeRepository;
	private final PostRepository postRepository;
	private final BookmarkRepository bookmarkRepository;
	private final CommentRepository commentRepository;
	private final NotificationFeignService notificationFeignService;
	private final ProfileRepository profileRepository;

	// 블로그 포스트 목록 조회
	@Transactional(readOnly = true)
	public PostsResponse getPostsOf(Long memberId, Long lastId) {
		// lastId 검증
		validateLastId(lastId);
		// 회원 프로필 검증
		validateProfileExists(memberId);
		// 게시물 조회
		List<Post> postList = postRepository.findTop10ByOrderByIdDesc();
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
	// lastId 값 검증 메서드
	private void validateLastId(Long lastId) {
		if (lastId < 0) {
			throw new ApiException(ApiCode.INVALID_DATA);
		}
	}
	// 회원 존재 여부 검증 메서드
	private void validateProfileExists(Long memberId) {
		boolean exists = profileRepository.existsById(memberId);
		if (!exists) {
			throw new ApiException(ApiCode.MEMBER_NOT_FOUND);
		}
	}
	// 게시물 내용 400자 제한
	private String truncateContent(String content) {
		return content.length() > 400 ? content.substring(0, 400) : content;
	}

	@Transactional(readOnly = true)
	public PostDetailResponse getPostById(Long postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));

		List<Comment> comments = commentRepository.findByPostId(postId);

		boolean isBookmarked = bookmarkRepository.existsByPostAndProfile(post, new Profile(1L, "", "", "", "", ""));

		return PostDetailResponse.of(post, comments, isBookmarked);
	}

	// 좋아요 토글
	@Transactional
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
}

