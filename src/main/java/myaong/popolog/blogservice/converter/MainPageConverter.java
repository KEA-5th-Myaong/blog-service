package myaong.popolog.blogservice.converter;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.dto.response.MainPageResponse;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.service.BookmarkService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static myaong.popolog.blogservice.entity.QBookmark.bookmark;
import static myaong.popolog.blogservice.entity.QPost.post;

@Component
@RequiredArgsConstructor
public class MainPageConverter {

	private final BookmarkService bookmarkService;

	public MainPageResponse toMainPageResponse(List<Post> postList) {

		List<MainPageResponse.PostDTO> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Post p : postList) {

			Long postId = p.getId();

			if (postId.compareTo(minId) < 0) {
				minId = postId;
			}

			MainPageResponse.PostDTO post = toMainPageResponse_Post(p, false);
			posts.add(post);
		}

		if (postList.size() < 10)
			minId = -1L;

		return MainPageResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}

	public MainPageResponse toMainPageResponse(List<Post> postList, Profile profile) {

		List<MainPageResponse.PostDTO> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Post p : postList) {

			Long postId = p.getId();

			if (postId.compareTo(minId) < 0) {
				minId = postId;
			}

			MainPageResponse.PostDTO post
					= toMainPageResponse_Post(p, bookmarkService.existsByProfileAndBookmarkId(postId, profile));
			posts.add(post);
		}

		if (postList.size() < 10)
			minId = -1L;

		return MainPageResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}

	public MainPageResponse toMainPageResponseBookmarked(List<Tuple> tupleList) {

		List<MainPageResponse.PostDTO> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Tuple tuple : tupleList) {

			Long bookmarkId = tuple.get(bookmark.id);
			if (bookmarkId.compareTo(minId) < 0) {
				minId = bookmarkId;
			}

			Post p = tuple.get(post);
			MainPageResponse.PostDTO post = toMainPageResponse_Post(p, true);
			posts.add(post);
		}

		if (tupleList.size() < 10)
			minId = -1L;

		return MainPageResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}

	private MainPageResponse.PostDTO toMainPageResponse_Post(Post post, Boolean isBookmarked) {

		return MainPageResponse.PostDTO.builder()
				.postId(post.getId())
				.title(post.getTitle())
				.content(post.getContent())
				.timestamp(post.getCreatedAt())
				.memberId(post.getProfile().getId())
				.username(post.getProfile().getUsername())
				.nickname(post.getProfile().getNickname())
				.profilePicUrl(post.getProfile().getProfilePicUrl())
				.isBookmarked(isBookmarked)
				.build();
	}
}
