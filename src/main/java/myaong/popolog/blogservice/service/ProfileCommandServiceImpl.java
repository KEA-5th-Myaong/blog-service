package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.converter.MemberProfileConverter;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.Follow;
import myaong.popolog.blogservice.entity.MemberProfile;
import myaong.popolog.blogservice.repository.FollowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileCommandServiceImpl implements ProfileCommandService {

	private final ProfileQueryService profileQueryService;
	private final FollowRepository followRepository;

	@Override
	public ProfileResponse.FollowDTO followMember(Long memberId) {
		// 일단 팔로우하는 사람은 id가 5인 member
		MemberProfile followingMember = profileQueryService.findMemberByMemberId(5L);
		MemberProfile followedMember = profileQueryService.findMemberByMemberId(memberId);

		boolean isExist = followRepository.existsByFollowingAndFollowed(followingMember, followedMember);

		ProfileResponse.FollowDTO followDTO;

		// 팔로우 내역이 이미 존재하면 팔로우 취소
		if (isExist) {
			followRepository.deleteByFollowingAndFollowed(followingMember, followedMember);
			followDTO = MemberProfileConverter.toFollowDTO(false);

		} else { // 새로 팔로우 정보 등록
			Follow follow = MemberProfileConverter.toFollow(followingMember, followedMember);
			followRepository.save(follow);
			followDTO = MemberProfileConverter.toFollowDTO(true);
		}

		return followDTO;
	}
}
