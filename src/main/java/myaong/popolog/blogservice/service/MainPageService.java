package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.response.MainPageResponse;

import java.util.List;

public interface MainPageService {

	MainPageResponse getRecentPosts(Long lastId);

	MainPageResponse getRecommendPosts(Long memberId, List<Long> preJobs, Long lastId);

	MainPageResponse getFollowingPosts(Long memberId, Long lastId);

	MainPageResponse getBookmarkedPosts(Long memberId, Long lastId);

	MainPageResponse searchPosts(Long memberId, String query, Long lastId);
}
