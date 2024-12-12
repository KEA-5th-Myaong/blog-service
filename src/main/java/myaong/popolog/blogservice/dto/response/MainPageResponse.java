package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class MainPageResponse {

	private Long lastId;
	private List<PostDTO> posts;

	@Builder
	@Getter
	public static class PostDTO {
		private Long postId;
		private String title;
		private String content;
		private LocalDateTime timestamp;
		private Long memberId;
		private String username;
		private String nickname;
		private String profilePicUrl;
		private List<String> prejob;
		private Long likeCount;
		private Boolean isBookmarked;
	}
}
