package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.request.NewProfileRequest;
import myaong.popolog.blogservice.dto.request.UpdateProfileRequest;
import myaong.popolog.blogservice.dto.response.ProfilePicUrlResponse;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileCommandService {

	void createProfile(NewProfileRequest newProfileRequest);

	ProfilePicUrlResponse updateProfilePic(Long memberId);

	ProfilePicUrlResponse updateProfilePic(Long memberId, MultipartFile pic);

	void updateProfile(Long memberId, UpdateProfileRequest req);

	ProfileResponse.FollowDTO followProfile(Long memberId);
}
