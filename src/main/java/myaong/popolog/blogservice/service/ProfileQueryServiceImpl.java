package myaong.popolog.blogservice.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.converter.ProfileConverter;
import myaong.popolog.blogservice.dto.response.FollowersResponse;
import myaong.popolog.blogservice.dto.response.FollowingsResponse;
import myaong.popolog.blogservice.dto.response.ProfileInfoResponse;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
	public ProfileInfoResponse getProfileInfo(Long requesterId, Long memberId) {

		Profile requester = findById(requesterId);
		Profile profile = findById(memberId);

		return profileConverter.toProfileInfoResponse(requester, profile);
	}

	@Override
	public ProfileResponse getProfileByUsername(String username) {

		Profile profile = findByUsername(username);

		return profileConverter.toProfileResponseByUsername(profile);
	}

	/***** follow 관련 메소드 *****/

	@Override
	public FollowingsResponse getProfileFollowingList(Long requesterId, Long memberId, Long lastId) {

		Profile requester = requesterId != null
				? findById(requesterId)
				: null;

		Profile member = findById(memberId);

		List<Tuple> tupleList = profileRepository.findProfilesFollowedBy(member, lastId.equals(0L) ? null : lastId);

		return profileConverter.toFollowingsResponse(tupleList, requester);
	}

	@Override
	public FollowersResponse getProfileFollowedList(Long requesterId, Long memberId, Long lastId) {

		Profile requester = requesterId != null
				? findById(requesterId)
				: null;

		Profile member = findById(memberId);

		List<Tuple> tupleList = profileRepository.findProfilesFollowing(member, lastId.equals(0L) ? null : lastId);

		return profileConverter.toFollowersResponse(tupleList, requester);
	}

	@Override
	public List<Profile> findFollowingOf(Profile member) {
		return member.getFollowings().stream().map(Follow::getFollowed).toList();
	}
}
