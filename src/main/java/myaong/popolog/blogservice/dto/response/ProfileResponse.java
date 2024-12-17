package myaong.popolog.blogservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfileResponse {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long memberId;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String username;
	private String name;
	private String nickname;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String profilePicUrl;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String blogIntro;
}
