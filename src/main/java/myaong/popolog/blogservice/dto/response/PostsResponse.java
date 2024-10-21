package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostsResponse {

	private Long lastId;
	private List<Posts> posts;

	@Builder
	@Getter
	public static class Posts {
		private Long postId;
		private String title;
		private String thumbnailUrl;
		private String content;
		private LocalDateTime timestamp;
		private Long memberId;
		private String username;
		private String nickname;
		private String profilePicUrl;
		private Boolean isBookmarked;
	}
}
