package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostsResponse {

	Long lastId;
	List<Posts> posts;

	@Getter
	public static class Posts {
		Long postId;
		String title;
		String thumbnailUrl;
		String content;
		LocalDateTime timestamp;
		Long memberId;
		String nickname;
		String profilePicUrl;
		Boolean isBookmarked;

		@Builder
		public Posts(Long postId, String title, String thumbnailUrl, String content, LocalDateTime timestamp, Long memberId, String nickname, String profilePicUrl, Boolean isBookmarked) {
			this.postId = postId;
			this.title = title;
			this.thumbnailUrl = thumbnailUrl;
			this.content = content;
			this.timestamp = timestamp;
			this.memberId = memberId;
			this.nickname = nickname;
			this.profilePicUrl = profilePicUrl;
			this.isBookmarked = isBookmarked;
		}
	}

	@Builder
	public PostsResponse(Long lastId, List<Posts> posts) {
		this.lastId = lastId;
		this.posts = posts;
	}
}
