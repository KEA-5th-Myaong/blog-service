package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.converter.ProfileConverter;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.repository.FollowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileCommandServiceImpl implements ProfileCommandService {

	private final ProfileQueryService profileQueryService;
	private final FollowRepository followRepository;
	private final ProfileConverter profileConverter;

	@Override
	public Boolean existsById(Long memberId) {
		return profileRepository.existsById(memberId);
	}

	@Override
	public Profile findById(Long memberId) {
		return profileRepository.findById(memberId)
				.orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND));
	}

	@Override
	public Profile findByUsername(String username) {
		return profileRepository.findByUsername(username)
				.orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND));
	}

	@Override
	public void createProfile(NewProfileRequest newProfileRequest) {

		if (existsById(newProfileRequest.getMemberId())) {
			throw new ApiException(ApiCode.MEMBER_CONFLICT);
		}

		Profile profile = profileConverter.fromNewProfileRequest(newProfileRequest);
		profileRepository.save(profile);
	}

	@Override
	public ProfileResponse.FollowDTO followProfile(Long memberId) {
		// 일단 팔로우하는 사람은 id가 5인 member
		Profile followingProfile = profileQueryService.findProfileByMemberId(5L);
		Profile followedProfile = profileQueryService.findProfileByMemberId(memberId);

		boolean isExist = followRepository.existsByFollowingAndFollowed(followingProfile, followedProfile);

		ProfileResponse.FollowDTO followDTO;

		// 팔로우 내역이 이미 존재하면 팔로우 취소
		if (isExist) {
			followRepository.deleteByFollowingAndFollowed(followingProfile, followedProfile);
			followDTO = ProfileConverter.toFollowDTO(false);

		} else { // 새로 팔로우 정보 등록
			Follow follow = ProfileConverter.toFollow(followingProfile, followedProfile);
			followRepository.save(follow);
			followDTO = ProfileConverter.toFollowDTO(true);
		}

		return followDTO;
	}
}
