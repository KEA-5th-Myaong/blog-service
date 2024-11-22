package myaong.popolog.blogservice.converter;

import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileConverter {

	public static ProfileResponse.PartialInfoDTO toPartialInfoDTO(Profile profile) {
		return ProfileResponse.PartialInfoDTO.builder()
				.memberId(profile.getId())
				.username(profile.getUsername())
				.nickname(profile.getNickname())
				.build();

	}

	public static ProfileResponse.FollowDTO toFollowDTO(boolean following) {
		return ProfileResponse.FollowDTO.builder()
				.following(following)
				.build();
	}

	public static Follow toFollow(Profile followingProfile, Profile followedProfile) {
		return Follow.builder()
				.following(followingProfile)
				.followed(followedProfile)
				.build();
	}

	public static ProfileResponse.FollowingListDTO toFollowingListDTO(List<Profile> profileList) {
		List<ProfileResponse.FollowingDTO> followingDTOList = profileList.stream()
				.map(member -> ProfileConverter.toFollowingDTO(member))
				.collect(Collectors.toList());

		return ProfileResponse.FollowingListDTO.builder()
				.lastId(0L)
				.followingDTOList(followingDTOList)
				.build();
	}

	public static ProfileResponse.FollowingDTO toFollowingDTO(Profile profile) {
		return ProfileResponse.FollowingDTO.builder()
				.memberId(profile.getId())
				.nickname(profile.getNickname())
				.profilePicUrl(profile.getProfilePicUrl())
				.isFollowed(true)
				.build();
	}

	public static ProfileResponse.FollowedListDTO toFollowedListDTO(List<Profile> profileList) {
		List<ProfileResponse.FollowedDTO> followedDTOList = profileList.stream()
				.map(member -> ProfileConverter.toFollowedDTO(member))
				.collect(Collectors.toList());

		return ProfileResponse.FollowedListDTO.builder()
				.lastId(0L)
				.followedDTOList(followedDTOList)
				.build();
	}

	public static ProfileResponse.FollowedDTO toFollowedDTO(Profile profile) {
		return ProfileResponse.FollowedDTO.builder()
				.memberId(profile.getId())
				.nickname(profile.getNickname())
				.profilePicUrl(profile.getProfilePicUrl())
				.isFollowed(false)
				.build();
	}

	public Profile fromNewProfileRequest(NewProfileRequest request) {
		return Profile.builder()
				.id(request.getMemberId())
				.username(request.getUsername())
				.name(request.getName())
				.nickname(request.getNickname())
				.blogIntro(String.format("%s의 블로그입니다.", request.getNickname()))
				.build();
	}
}
