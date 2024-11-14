package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.repository.BookmarkRepository;
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
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getProfile().getId())
					.username(p.getProfile().getUsername())
					.nickname(p.getProfile().getNickname())
					.profilePicUrl(p.getProfile().getProfilePicUrl())
					.isBookmarked(bookmarkRepository.existsByIdAndProfile(postId, new Profile(memberId, "", "", "", "", "")))
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
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getProfile().getId())
					.username(p.getProfile().getUsername())
					.nickname(p.getProfile().getNickname())
					.profilePicUrl(p.getProfile().getProfilePicUrl())
					.isBookmarked(bookmarkRepository.existsByIdAndProfile(postId, new Profile(memberId, "", "", "", "", "")))
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
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getProfile().getId())
					.username(p.getProfile().getUsername())
					.nickname(p.getProfile().getNickname())
					.profilePicUrl(p.getProfile().getProfilePicUrl())
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
