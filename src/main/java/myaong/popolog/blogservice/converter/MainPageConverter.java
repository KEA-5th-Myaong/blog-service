package myaong.popolog.blogservice.converter;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.dto.response.MainPageResponse;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.service.BookmarkService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MainPageConverter {

	private final BookmarkService bookmarkService;

	public MainPageResponse toMainPageResponse(List<Post> postList) {

		List<MainPageResponse.Posts> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Post p : postList) {

			Long postId = p.getId();

			if (postId.compareTo(minId) < 0) {
				minId = postId;
			}
			MainPageResponse.Posts post = MainPageResponse.Posts.builder()
					.postId(postId)
					.title(p.getTitle())
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getProfile().getId())
					.username(p.getProfile().getUsername())
					.nickname(p.getProfile().getNickname())
					.profilePicUrl(p.getProfile().getProfilePicUrl())
					.isBookmarked(false)
					.build();
			posts.add(post);
		}

		if (postList.size() < 10)
			minId = -1L;

		return MainPageResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}

	public MainPageResponse toMainPageResponse(List<Post> postList, Profile profile) {

		List<MainPageResponse.Posts> posts = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (Post p : postList) {

			Long postId = p.getId();

			if (postId.compareTo(minId) < 0) {
				minId = postId;
			}
			MainPageResponse.Posts post = MainPageResponse.Posts.builder()
					.postId(postId)
					.title(p.getTitle())
					.content(p.getContent())
					.timestamp(p.getCreatedAt())
					.memberId(p.getProfile().getId())
					.username(p.getProfile().getUsername())
					.nickname(p.getProfile().getNickname())
					.profilePicUrl(p.getProfile().getProfilePicUrl())
					.isBookmarked(bookmarkService.existsByProfileAndBookmarkId(postId, profile))
					.build();
			posts.add(post);
		}

		if (postList.size() < 10)
			minId = -1L;

		return MainPageResponse.builder()
				.lastId(minId)
				.posts(posts).build();
	}
}
