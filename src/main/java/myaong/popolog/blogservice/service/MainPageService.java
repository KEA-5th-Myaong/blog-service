package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.response.MainPageResponse;

public interface MainPageService {
	MainPageResponse getRecentPosts(Long lastId);
}
