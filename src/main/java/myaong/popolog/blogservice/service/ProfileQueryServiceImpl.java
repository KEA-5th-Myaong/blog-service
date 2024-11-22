package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.converter.ProfileConverter;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileQueryServiceImpl implements ProfileQueryService {

	private final ProfileRepository profileRepository;
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
	public ProfileResponse getProfileByMemberId(Long memberId) {

		Profile profile = findById(memberId);

		return profileConverter.toProfileResponseByMemberId(profile);
	}

	@Override
	public ProfileResponse getProfileByUsername(String username) {

		Profile profile = findByUsername(username);

		return profileConverter.toProfileResponseByUsername(profile);
	}

	@Override
	public ProfileResponse.FollowingListDTO getProfileFollowingList(Long memberId, Long lastId) {
		// 1부터 10까지의 ID 리스트 생성
		List<Long> ids = LongStream.rangeClosed(1, 10)
				.boxed()
				.collect(Collectors.toList());

		List<Profile> findProfileList = profileRepository.findByIdIn(ids);

		return ProfileConverter.toFollowingListDTO(findProfileList);
	}

	@Override
	public ProfileResponse.FollowedListDTO getProfileFollowedList(Long memberId, Long lastId) {
		// 1부터 10까지의 ID 리스트 생성
		List<Long> ids = LongStream.rangeClosed(1, 10)
				.boxed()
				.collect(Collectors.toList());

		List<Profile> findProfileList = profileRepository.findByIdIn(ids);

		return ProfileConverter.toFollowedListDTO(findProfileList);
	}

	@Override
	public Profile findProfileByMemberId(Long memberId) {
		return profileRepository.findById(memberId)
				.orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND));
	}

	@Override
	public List<Profile> findFollowingOf(Profile member) {
		return member.getFollowings().stream().map(Follow::getFollowed).toList();
	}
}
