package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.repository.FollowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowQueryServiceImpl implements FollowQueryService {

	private final FollowRepository followRepository;

	@Override
	public Boolean existsByMembers(Profile requester, Profile profile) {

		return followRepository.existsByFollowingAndFollowed(requester, profile);
	}
}
