package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostsResponse {

	private Long lastId; // 마지막 게시물 ID
	private List<Posts> posts; // 게시물 목록

	@Builder
	@Getter
	public static class Posts {
		private Long postId;
		private String title;
		private String content; // 400자 이내로 자른 텍스트
		private LocalDateTime timestamp; // 작성 시간
		private int likeCount; // 좋아요 수
		private Boolean isBookmarked; // 북마크 여부

		public static class PostsBuilder {
			public PostsBuilder content(String content) {
				this.content = content.length() > 400 ? content.substring(0, 400) : content;
				return this;
			}
		}
	}
}
