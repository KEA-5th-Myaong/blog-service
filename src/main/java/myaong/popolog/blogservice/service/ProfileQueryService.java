package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.response.FollowersResponse;
import myaong.popolog.blogservice.dto.response.FollowingsResponse;
import myaong.popolog.blogservice.dto.response.ProfileInfoResponse;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Profile;

import java.util.List;

public interface ProfileQueryService {

	Boolean existsById(Long memberId);

	Profile findById(Long memberId);

	Profile findByUsername(String username);

	ProfileResponse getProfileByMemberId(Long memberId);

	ProfileInfoResponse getProfileInfo(Long requesterId, Long memberId);

	ProfileResponse getProfileByUsername(String username);

	/***** follow 관련 메소드 *****/

	FollowingsResponse getProfileFollowingList(Long requesterId, Long memberId, Long lastId);

	FollowersResponse getProfileFollowedList(Long requesterId, Long memberId, Long lastId);

	/**
	 * 해당 회원을 팔로우하는 회원 목록
	 */
	List<Profile> findFollowingOf(Profile member);
}
