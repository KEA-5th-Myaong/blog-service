package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

	@NotBlank(message = "닉네임을 작성해주세요.")
	private String nickname;
	@NotBlank(message = "블로그 소개글을 작성해주세요.")
	private String blogIntro;
}
