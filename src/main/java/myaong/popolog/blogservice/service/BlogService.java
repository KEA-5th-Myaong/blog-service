package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.repository.BookmarkRepository;
import myaong.popolog.blogservice.repository.CommentRepository;
import myaong.popolog.blogservice.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {

	private final PostRepository postRepository;
	private final BookmarkRepository bookmarkRepository;


	@Transactional(readOnly = true)
	public PostsResponse getRecent(Long memberId, Long lastId){

		List<Post> postList = postRepository.findTop10ByOrderByIdDesc();

		List<PostsResponse.Posts> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Post p : postList) {

			Long postId = p.getId();

			if (postId.compareTo(minId) < 0) {
				minId = postId;
			}
			PostsResponse.Posts post = PostsResponse.Posts.builder()
					.postId(postId)
					.title(p.getTitle())
					.thumbnailUrl(p.getThumbnailUrl())
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getMemberProfile().getId())
					.username(p.getMemberProfile().getUsername())
					.nickname(p.getMemberProfile().getNickname())
					.profilePicUrl(p.getMemberProfile().getProfilePicUrl())
					.isBookmarked(bookmarkRepository.existsByIdAndMemberId(postId, memberId))
					.build();
			posts.add(post);
		}

		if (postList.size() < 10)
			minId = -1L;

		return PostsResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}


	public PostsResponse search(Long memberId, String search, Long lastId){

		List<Post> postList = postRepository.findTop10ByOrderByIdDesc();

		List<PostsResponse.Posts> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Post p : postList) {

			Long postId = p.getId();

			if (postId.compareTo(minId) < 0) {
				minId = postId;
			}
			PostsResponse.Posts post = PostsResponse.Posts.builder()
					.postId(postId)
					.title(p.getTitle())
					.thumbnailUrl(p.getThumbnailUrl())
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getMemberProfile().getId())
					.username(p.getMemberProfile().getUsername())
					.nickname(p.getMemberProfile().getNickname())
					.profilePicUrl(p.getMemberProfile().getProfilePicUrl())
					.isBookmarked(bookmarkRepository.existsByIdAndMemberId(postId, memberId))
					.build();
			posts.add(post);
		}

		if (postList.size() < 10)
			minId = -1L;

		return PostsResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}

	@Transactional(readOnly = true)
	public PostsResponse getRecommend(Long memberId, List<Long> preJob, Long lastId) {

		List<Post> postList = postRepository.findTop10ByOrderByIdDesc();

		List<PostsResponse.Posts> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Post p : postList) {

			Long postId = p.getId();

			if (postId.compareTo(minId) < 0) {
				minId = postId;
			}
			PostsResponse.Posts post = PostsResponse.Posts.builder()
					.postId(postId)
					.title(p.getTitle())
					.thumbnailUrl(p.getThumbnailUrl())
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getMemberProfile().getId())
					.username(p.getMemberProfile().getUsername())
					.nickname(p.getMemberProfile().getNickname())
					.profilePicUrl(p.getMemberProfile().getProfilePicUrl())
					.isBookmarked(bookmarkRepository.existsByIdAndMemberId(postId, memberId))
					.build();
			posts.add(post);
		}

		if (postList.size() < 10)
			minId = -1L;

		return PostsResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}

	@Transactional(readOnly = true)
	public PostsResponse getFollowing(Long memberId, Long lastId){

		List<Post> postList = postRepository.findTop10ByOrderByIdDesc();

		List<PostsResponse.Posts> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Post p : postList) {

			Long postId = p.getId();

			if (postId.compareTo(minId) < 0) {
				minId = postId;
			}
			PostsResponse.Posts post = PostsResponse.Posts.builder()
					.postId(postId)
					.title(p.getTitle())
					.thumbnailUrl(p.getThumbnailUrl())
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getMemberProfile().getId())
					.username(p.getMemberProfile().getUsername())
					.nickname(p.getMemberProfile().getNickname())
					.profilePicUrl(p.getMemberProfile().getProfilePicUrl())
					.isBookmarked(bookmarkRepository.existsByIdAndMemberId(postId, memberId))
					.build();
			posts.add(post);
		}

		if (postList.size() < 10)
			minId = -1L;

		return PostsResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}

	@Transactional(readOnly = true)
	public PostsResponse getBookmark(Long memberId, Long lastId){

		List<Post> postList = postRepository.findTop10ByOrderByIdDesc();

		List<PostsResponse.Posts> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Post p : postList) {

			Long postId = p.getId();

			if (postId.compareTo(minId) < 0) {
				minId = postId;
			}
			PostsResponse.Posts post = PostsResponse.Posts.builder()
					.postId(postId)
					.title(p.getTitle())
					.thumbnailUrl(p.getThumbnailUrl())
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getMemberProfile().getId())
					.username(p.getMemberProfile().getUsername())
					.nickname(p.getMemberProfile().getNickname())
					.profilePicUrl(p.getMemberProfile().getProfilePicUrl())
					.isBookmarked(true)
					.build();
			posts.add(post);
		}

		if (postList.size() < 10)
			minId = -1L;

		return PostsResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}
}
