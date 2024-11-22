package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class NewProfileRequest {

	@Positive(message = "memberId는 1 이상이어야 합니다.")
	private Long memberId;
	@NotBlank(message = "username이 빈칸일 수 없습니다.")
	private String username;
	@NotBlank(message = "name이 빈칸일 수 없습니다.")
	private String name;
	@NotBlank(message = "nickname이 빈칸일 수 없습니다.")
	private String nickname;
}
