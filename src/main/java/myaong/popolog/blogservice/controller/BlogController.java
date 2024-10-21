package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
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

	@Operation(summary = "API 명세서 v0.3 line 31", description = "최신 포스트 조회")
	@GetMapping("/recent/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getRecent(@PathVariable Long lastId) {

		PostsResponse res = blogService.getRecent(5L, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.3 line 35", description = "포스트 검색")
	@GetMapping("/search/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getSearch(@RequestParam(name = "search") String search,
																@PathVariable Long lastId) {

		PostsResponse res = blogService.search(5L, search, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.3 line 32", description = "추천 포스트 조회")
	@GetMapping("/recommend/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getRecommend(@RequestParam("preJob") String rawPreJob,
																   @PathVariable Long lastId) {

		List<Long> preJob = Arrays.stream(rawPreJob.split(",")).map(Long::valueOf).toList();

		PostsResponse res = blogService.getRecommend(5L, preJob, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.3 line 33", description = "팔로잉 포스트 조회")
	@GetMapping("/following/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getFollowing(@PathVariable Long lastId) {

		PostsResponse res = blogService.getFollowing(5L, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.3 line 34", description = "북마크 포스트 조회")
	@GetMapping("/bookmark/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getBookmark(@PathVariable Long lastId) {

		PostsResponse res = blogService.getBookmark(5L, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}
}
