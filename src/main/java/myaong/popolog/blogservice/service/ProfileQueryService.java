package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.MemberProfile;

public interface ProfileQueryService {

	ProfileResponse.FollowingListDTO getMemberFollowingList(Long memberId, Long lastId);

	ProfileResponse.FollowedListDTO getMemberFollowedList(Long memberId, Long lastId);

	MemberProfile findMemberByMemberId(Long memberId);
}
