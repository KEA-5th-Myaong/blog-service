package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.converter.MemberProfileConverter;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.entity.MemberProfile;
import myaong.popolog.blogservice.repository.MemberProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileQueryServiceImpl implements ProfileQueryService {

	private final MemberProfileRepository memberProfileRepository;

	@Override
	public ProfileResponse.FollowingListDTO getMemberFollowingList(Long memberId, Long lastId) {
		// 1부터 10까지의 ID 리스트 생성
		List<Long> ids = LongStream.rangeClosed(1, 10)
				.boxed()
				.collect(Collectors.toList());

		List<MemberProfile> findMemberList = memberProfileRepository.findByIdIn(ids);

		return MemberProfileConverter.toFollowingListDTO(findMemberList);
	}

	@Override
	public ProfileResponse.FollowedListDTO getMemberFollowedList(Long memberId, Long lastId) {
		// 1부터 10까지의 ID 리스트 생성
		List<Long> ids = LongStream.rangeClosed(1, 10)
				.boxed()
				.collect(Collectors.toList());

		List<MemberProfile> findMemberList = memberProfileRepository.findByIdIn(ids);

		return MemberProfileConverter.toFollowedListDTO(findMemberList);
	}

	@Override
	public MemberProfile findMemberByMemberId(Long memberId) {
		return memberProfileRepository.findById(memberId)
				.orElseThrow(() -> new ApiException(ApiCode.MEMBER_NOT_FOUND));
	}
}
