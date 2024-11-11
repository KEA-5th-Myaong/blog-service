package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.converter.MainPageConverter;
import myaong.popolog.blogservice.dto.response.MainPageResponse;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainPageServiceImpl implements MainPageService {

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
}
