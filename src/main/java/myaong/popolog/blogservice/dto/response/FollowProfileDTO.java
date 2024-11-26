package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowProfileDTO {

	private Long memberId;
	private String nickname;
	private String profilePicUrl;
	private boolean isFollowing;
}
