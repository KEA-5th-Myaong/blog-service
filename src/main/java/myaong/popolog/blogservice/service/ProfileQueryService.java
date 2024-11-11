package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Profile;

public interface ProfileQueryService {

	ProfileResponse.FollowingListDTO getProfileFollowingList(Long memberId, Long lastId);

	ProfileResponse.FollowedListDTO getProfileFollowedList(Long memberId, Long lastId);

	Profile findProfileByMemberId(Long memberId);
}
