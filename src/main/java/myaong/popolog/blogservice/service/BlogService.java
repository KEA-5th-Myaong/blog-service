package myaong.popolog.blogservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.dto.request.PageRequest;
import myaong.popolog.blogservice.dto.request.RecommendRequest;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.repository.BookmarkRepository;
import myaong.popolog.blogservice.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogService {

	private final PostRepository postRepository;
	private final BookmarkRepository bookmarkRepository;

	public PostsResponse getRecommend(Long memberId, RecommendRequest request) {

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

	public PostsResponse getFollowing(Long memberId, PageRequest request){

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

	public PostsResponse getBookmark(Long memberId, PageRequest request){

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