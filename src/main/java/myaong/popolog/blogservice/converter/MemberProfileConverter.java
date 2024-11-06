package myaong.popolog.blogservice.converter;

import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.MemberProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberProfileConverter {

	public static ProfileResponse.PartialInfoDTO toPartialInfoDTO(MemberProfile member) {
		return ProfileResponse.PartialInfoDTO.builder()
				.memberId(member.getId())
				.username(member.getUsername())
				.nickname(member.getNickname())
				.build();

	}

	public static ProfileResponse.FollowDTO toFollowDTO(boolean following) {
		return ProfileResponse.FollowDTO.builder()
				.following(following)
				.build();
	}

	public static Follow toFollow(MemberProfile followingMember, MemberProfile followedMember) {
		return Follow.builder()
				.following(followingMember)
				.followed(followedMember)
				.build();
	}

	public static ProfileResponse.FollowingListDTO toFollowingListDTO(List<MemberProfile> memberList) {
		List<ProfileResponse.FollowingDTO> followingDTOList = memberList.stream()
				.map(member -> MemberProfileConverter.toFollowingDTO(member))
				.collect(Collectors.toList());

		return ProfileResponse.FollowingListDTO.builder()
				.lastId(0L)
				.followingDTOList(followingDTOList)
				.build();
	}

	public static ProfileResponse.FollowingDTO toFollowingDTO(MemberProfile member) {
		return ProfileResponse.FollowingDTO.builder()
				.memberId(member.getId())
				.nickname(member.getNickname())
				.profilePicUrl(member.getProfilePicUrl())
				.isFollowed(true)
				.build();
	}

	public static ProfileResponse.FollowedListDTO toFollowedListDTO(List<MemberProfile> memberList) {
		List<ProfileResponse.FollowedDTO> followedDTOList = memberList.stream()
				.map(member -> MemberProfileConverter.toFollowedDTO(member))
				.collect(Collectors.toList());

		return ProfileResponse.FollowedListDTO.builder()
				.lastId(0L)
				.followedDTOList(followedDTOList)
				.build();
	}

	public static ProfileResponse.FollowedDTO toFollowedDTO(MemberProfile member) {
		return ProfileResponse.FollowedDTO.builder()
				.memberId(member.getId())
				.nickname(member.getNickname())
				.profilePicUrl(member.getProfilePicUrl())
				.isFollowed(false)
				.build();
	}
}
