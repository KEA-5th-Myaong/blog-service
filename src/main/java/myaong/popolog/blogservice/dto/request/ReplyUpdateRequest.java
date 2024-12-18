package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReplyUpdateRequest {

	@NotBlank(message = "답글 내용을 작성해주세요.")
	private String content;
}
