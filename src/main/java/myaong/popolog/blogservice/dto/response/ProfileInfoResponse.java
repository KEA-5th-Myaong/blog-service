package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProfileInfoResponse {

	private Long memberId;
	private String nickname;
	private String username;
	private String blogIntro;
	private Boolean isFollowing;
	private Integer followingCount;
	private Integer followerCount;
	private String profilePicUrl;
	private List<String> prejob;
}
