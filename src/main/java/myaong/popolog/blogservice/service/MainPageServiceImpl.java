package myaong.popolog.blogservice.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.converter.MainPageConverter;
import myaong.popolog.blogservice.dto.response.MainPageResponse;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Prejob;
import myaong.popolog.blogservice.entity.Profile;
import myaong.popolog.blogservice.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainPageServiceImpl implements MainPageService {

	private final ProfileQueryService profileQueryService;
	private final PostRepository postRepository;
	private final MainPageConverter mainPageConverter;

	@Override
	public MainPageResponse getRecentPosts(Long lastId) {

		List<Post> postList;
		if (lastId.equals(0L)) {
			postList = postRepository.findTop10ByOrderByIdDesc();
		} else {
			postList = postRepository.findTop10ByIdLessThanOrderByIdDesc(lastId);
		}

		return mainPageConverter.toMainPageResponse(postList);
	}

	@Override
	public MainPageResponse getRecommendPosts(Long memberId, List<Long> preJobs, Long lastId) {

		Profile member = profileQueryService.findProfileByMemberId(memberId);

		// 요청된 관심직군 중 사용자의 관심 직군이 아닌 것이 있는지 검증
		List<Long> prejobsOfMember = member.getPrejobs().stream().map(Prejob::getId).toList();
		preJobs.forEach((p) -> {
			if (!prejobsOfMember.contains(p)) throw new ApiException(ApiCode.INVALID_PREJOBS);
		});

		List<Post> postList;
		if (lastId.equals(0L)) {
			postList = postRepository.findByPrejobExcludingProfile(member, preJobs);
		} else {
			postList = postRepository.findByPrejobExcludingProfile(member, preJobs, lastId);
		}

		return mainPageConverter.toMainPageResponse(postList, member);
	}

	@Override
	public MainPageResponse getFollowingPosts(Long memberId, Long lastId) {

		Profile member = profileQueryService.findProfileByMemberId(memberId);

		List<Post> postList;
		if (lastId.equals(0L)) {
			postList = postRepository.findByFollowing(member);
		} else {
			postList = postRepository.findByFollowing(member, lastId);
		}

		return mainPageConverter.toMainPageResponse(postList);
	}

	@Override
	public MainPageResponse getBookmarkedPosts(Long memberId, Long lastId) {

		Profile member = profileQueryService.findProfileByMemberId(memberId);

		List<Tuple> tupleList;
		if (lastId.equals(0L)) {
			tupleList = postRepository.findByProfile_Bookmark(member);
		} else {
			tupleList = postRepository.findByProfile_Bookmark(member, lastId);
		}

		return mainPageConverter.toMainPageResponseBookmarked(tupleList);
	}
}
