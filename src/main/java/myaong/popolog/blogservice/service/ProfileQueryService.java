package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Profile;

import java.util.List;

public interface ProfileQueryService {

	ProfileResponse.FollowingListDTO getProfileFollowingList(Long memberId, Long lastId);

	ProfileResponse.FollowedListDTO getProfileFollowedList(Long memberId, Long lastId);

	Profile findProfileByMemberId(Long memberId);

	/**
	 * 해당 회원을 팔로우하는 회원 목록
	 */
	List<Profile> findFollowingOf(Profile member);
}
