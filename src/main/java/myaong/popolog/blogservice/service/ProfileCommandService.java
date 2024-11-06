package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.response.ProfileResponse;

public interface ProfileCommandService {

	ProfileResponse.FollowDTO followMember(Long memberId);
}
