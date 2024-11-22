package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.request.NewProfileRequest;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Profile;

public interface ProfileCommandService {

	Boolean existsById(Long memberId);

	Profile findById(Long memberId);

	Profile findByUsername(String username);

	void createProfile(NewProfileRequest newProfileRequest);

	ProfileResponse.FollowDTO followProfile(Long memberId);
}
