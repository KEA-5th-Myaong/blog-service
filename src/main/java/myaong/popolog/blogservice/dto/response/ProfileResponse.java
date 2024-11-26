package myaong.popolog.blogservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ProfileResponse {

	private Long memberId;	// nullable
	private String username;	// nullable
	private String name;
	private String nickname;
	private String profilePicUrl;	// nullable
	private String blogIntro;	// nullable

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PartialInfoDTO {
		private Long memberId;
		private String username;
		private String nickname;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FollowDTO {
		private boolean following;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FollowingDTO {
		private Long memberId;
		private String nickname;
		private String profilePicUrl;
		private boolean isFollowed;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FollowingListDTO {
		private Long lastId;
		private List<FollowingDTO> followingDTOList = new ArrayList<>();
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FollowedDTO {
		private Long memberId;
		private String nickname;
		private String profilePicUrl;
		private boolean isFollowed;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FollowedListDTO {
		private Long lastId;
		private List<FollowedDTO> followedDTOList = new ArrayList<>();
	}
}
