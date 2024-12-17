package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.Prefix;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.converter.ProfileConverter;
import myaong.popolog.blogservice.dto.request.NewProfileRequest;
import myaong.popolog.blogservice.dto.request.PrejobsRequest;
import myaong.popolog.blogservice.dto.request.UpdateProfileRequest;
import myaong.popolog.blogservice.dto.response.FollowResponse;
import myaong.popolog.blogservice.dto.response.ProfilePicUrlResponse;
import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.Prejob;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.feign.constant.NotificationType;
import myaong.popolog.blogservice.feign.service.NotificationFeignService;
import myaong.popolog.blogservice.repository.FollowRepository;
import myaong.popolog.blogservice.repository.PrejobRepository;
import myaong.popolog.blogservice.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileCommandServiceImpl implements ProfileCommandService {

	private final ProfileQueryService profileQueryService;
	private final ProfileRepository profileRepository;
	private final FollowRepository followRepository;
	private final ProfileConverter profileConverter;
	private final S3ApiService s3ApiService;
	private final NotificationFeignService notificationFeignService;
	private final PrejobRepository prejobRepository;

	@Override
	public void createProfile(NewProfileRequest newProfileRequest) {

		if (profileQueryService.existsById(newProfileRequest.getMemberId())) {
			throw new ApiException(ApiCode.MEMBER_CONFLICT);
		}

		Profile profile = profileConverter.fromNewProfileRequest(newProfileRequest);
		profileRepository.save(profile);
	}

	private void deleteProfilePic(Profile profile) {

		if (profile.getProfilePicUrl() != null) {
			s3ApiService.deleteFromPersistentStorage(profile.getProfilePicUrl());
		}
	}

	@Override
	public ProfilePicUrlResponse updateProfilePic(Long memberId) {

		Profile profile = profileQueryService.findById(memberId);
		deleteProfilePic(profile);

		profile.updateProfilePicUrl(null);
		profileRepository.save(profile);

		return null;
	}

	@Override
	public ProfilePicUrlResponse updateProfilePic(Long memberId, MultipartFile pic) {

		Profile profile = profileQueryService.findById(memberId);
		// 기존 프로필 사진이 있다면 삭제
		deleteProfilePic(profile);

		String profilePicUrl = s3ApiService.uploadToPersistentStorage(Prefix.PROFILE, pic);

		profile.updateProfilePicUrl(profilePicUrl);
		profileRepository.save(profile);

		return new ProfilePicUrlResponse(profilePicUrl);
	}

	@Override
	public void updateProfile(Long memberId, UpdateProfileRequest req) {

		Profile profile = profileQueryService.findById(memberId);
		profile.updateNameAndBlogIntro(req.getNickname(), req.getBlogIntro());
	}

	@Override
	public FollowResponse followProfile(Long requesterId, Long memberId) {

		// 자기 자신에 대해 요청할 수 없음
		if (requesterId.equals(memberId)) {
			throw new ApiException(ApiCode.SELF_FOLLOW_CONFLICT);
		}

		Profile followingProfile = profileQueryService.findById(requesterId);
		Profile followedProfile = profileQueryService.findById(memberId);

		boolean isExist = followRepository.existsByFollowingAndFollowed(followingProfile, followedProfile);

		FollowResponse followResponse;

		// 팔로우 내역이 이미 존재하면 팔로우 취소
		if (isExist) {
			followRepository.deleteByFollowingAndFollowed(followingProfile, followedProfile);
			followResponse = profileConverter.toFollowResponse(false);

		} else { // 새로 팔로우 정보 등록
			Follow follow = profileConverter.toFollow(followingProfile, followedProfile);
			followRepository.save(follow);
			followResponse = profileConverter.toFollowResponse(true);

			// 알림 전송 (직접 호출)
			String title = String.format("%s님이 당신을 팔로우합니다!", followingProfile.getNickname());
			String content = ""; //content가 notNull이라 임시조치
			String url = "/profiles/" + followingProfile.getId();

			notificationFeignService.sendNotification(
					followedProfile.getId(),
					title,
					content,
					url,
					NotificationType.FOLLOW,
					followingProfile.getId()
			);
		}
		return followResponse;
	}

	@Override
	public void createProfilePrejobs(Long memberId, List<PrejobsRequest> requests) {

		Profile profile = profileQueryService.findById(memberId);

		// 기존 prejobs 삭제
		prejobRepository.deleteByProfile(profile);

		// 요청된 prejobs 생성
		for (PrejobsRequest request : requests) {

			Prejob prejob = Prejob.builder()
					.id(request.getMemberPrejobId())
					.profile(profile)
					.jobId(request.getJobId())
					.jobName(request.getJobName())
					.build();
			prejobRepository.save(prejob);
		}
	}
}
