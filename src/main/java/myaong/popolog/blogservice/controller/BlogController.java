package myaong.popolog.blogservice.controller;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

	private final BlogService blogService;

	@GetMapping("/recent/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getRecent(@PathVariable Long lastId) {

		PostsResponse res = blogService.getRecent(5L, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@GetMapping("/search/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getSearch(@RequestParam(name = "search") String search,
																@PathVariable Long lastId) {

		PostsResponse res = blogService.search(5L, search, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@GetMapping("/recommend/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getRecommend(@RequestParam("preJob") String rawPreJob,
																   @PathVariable Long lastId) {

		List<Long> preJob = Arrays.stream(rawPreJob.split(",")).map(Long::valueOf).toList();

		PostsResponse res = blogService.getRecommend(5L, preJob, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@GetMapping("/following/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getFollowing(@PathVariable Long lastId) {

		PostsResponse res = blogService.getFollowing(5L, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@GetMapping("/bookmark/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getBookmark(@PathVariable Long lastId) {

		PostsResponse res = blogService.getBookmark(5L, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}
}
