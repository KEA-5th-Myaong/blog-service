package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.request.NewProfileRequest;
import myaong.popolog.blogservice.dto.response.ProfileResponse;

public interface ProfileCommandService {

	void createProfile(NewProfileRequest newProfileRequest);

	ProfileResponse.FollowDTO followProfile(Long memberId);
}
