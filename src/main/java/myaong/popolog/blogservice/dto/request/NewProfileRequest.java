package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class NewProfileRequest {

	@Positive(message = "요청된 회원 아이디로 프로필을 등록할 수 없습니다.")
	private Long memberId;
	@NotBlank(message = "아이디를 입력해 주세요.")
	private String username;
	@NotBlank(message = "이름을 입력해 주세요.")
	private String name;
	@NotBlank(message = "닉네임을 입력해 주세요.")
	private String nickname;
}
