package myaong.popolog.blogservice.converter;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.dto.SortedEntity;
import myaong.popolog.blogservice.dto.request.NewProfileRequest;
import myaong.popolog.blogservice.dto.response.*;
import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.Prejob;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.repository.FollowRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfileConverter {

	private final FollowRepository followRepository;

	public FollowResponse toFollowResponse(boolean following) {

		return FollowResponse.builder()
				.following(following)
				.build();
	}

	public Follow toFollow(Profile followingProfile, Profile followedProfile) {

		return Follow.builder()
				.following(followingProfile)
				.followed(followedProfile)
				.build();
	}

	public FollowingsResponse toFollowingsResponse(List<SortedEntity<Profile>> tupleList, Profile requester) {

		SortedEntity<List<FollowProfileDTO>> sortedEntity = toFollowProfileDTOList(tupleList, requester);

		return FollowingsResponse.builder()
				.lastId(sortedEntity.getKey())
				.followingDTOList(sortedEntity.getEntity())
				.build();
	}

	public FollowersResponse toFollowersResponse(List<SortedEntity<Profile>> tupleList, Profile requester) {

		SortedEntity<List<FollowProfileDTO>> sortedEntity = toFollowProfileDTOList(tupleList, requester);

		return FollowersResponse.builder()
				.lastId(sortedEntity.getKey())
				.followedDTOList(sortedEntity.getEntity())
				.build();
	}

	private SortedEntity<List<FollowProfileDTO>> toFollowProfileDTOList(List<SortedEntity<Profile>> tupleList, Profile requester) {

		List<FollowProfileDTO> profiles = new ArrayList<>();
		long minId = Long.MAX_VALUE;

		for (SortedEntity<Profile> tuple : tupleList) {

			Long followId = tuple.getKey();
			if (followId.compareTo(minId) < 0) {
				minId = followId;
			}

			Profile following = tuple.getEntity();
			FollowProfileDTO profileDTO = toFollowProfileDTO(following, requester);
			profiles.add(profileDTO);
		}

		if (tupleList.size() < 10)
			minId = -1L;

		return new SortedEntity<>(profiles, minId);
	}

	private FollowProfileDTO toFollowProfileDTO(Profile profile, Profile requester) {

		// 로그인한 경우, 해당 회원이 대상 회원을 팔로우하고 있는지의 여부를 표시
		// 로그인하지 않은 경우 팔로우 여부를 항상 false로 표시
		boolean isFollowing = requester != null && followRepository.existsByFollowingAndFollowed(requester, profile);

		return FollowProfileDTO.builder()
				.memberId(profile.getId())
				.nickname(profile.getNickname())
				.profilePicUrl(profile.getProfilePicUrl())
				.isFollowing(isFollowing)
				.build();
	}

	/***** follow 관련 메소드 (위) *****/

	public Profile fromNewProfileRequest(NewProfileRequest request) {

		return Profile.builder()
				.id(request.getMemberId())
				.username(request.getUsername())
				.name(request.getName())
				.nickname(request.getNickname())
				.blogIntro(String.format("%s의 블로그입니다.", request.getNickname()))
				.build();
	}

	public ProfileResponse toProfileResponseByMemberId(Profile profile) {

		return ProfileResponse.builder()
				.name(profile.getName())
				.nickname(profile.getNickname())
				.profilePicUrl(profile.getProfilePicUrl())
				.build();
	}

	public ProfileInfoResponse toProfileInfoResponse(Profile requester, Profile profile) {

		return ProfileInfoResponse.builder()
				.memberId(profile.getId())
				.nickname(profile.getNickname())
				.username(profile.getUsername())
				.blogIntro(profile.getBlogIntro())
				.isFollowing(followRepository.existsByFollowingAndFollowed(requester, profile))
				.followingCount(profile.getFollowings().size())
				.followerCount(profile.getFollowers().size())
				.profilePicUrl(profile.getProfilePicUrl())
				.prejob(profile.getPrejobs().stream().map(Prejob::getJobName).toList())
				.build();
	}

	public ProfileResponse toProfileResponseByUsername(Profile profile) {

		return ProfileResponse.builder()
				.memberId(profile.getId())
				.username(profile.getUsername())
				.nickname(profile.getNickname())
				.blogIntro(profile.getBlogIntro())
				.build();
	}
}
