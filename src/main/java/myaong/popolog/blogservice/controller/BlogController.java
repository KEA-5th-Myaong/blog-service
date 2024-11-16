package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.response.MainPageResponse;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.service.BlogService;
import myaong.popolog.blogservice.service.MainPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

	private final MainPageService mainPageService;
	private final BlogService blogService;

	@Operation(summary = "API 명세서 v0.4 line 25", description = "최신 포스트 조회")
	@GetMapping("/recent/{lastId}")
	public ResponseEntity<ApiResponse<MainPageResponse>> getRecent(@PathVariable @PositiveOrZero(message = "lastId는 0 이상이어야 합니다.") Long lastId) {

		MainPageResponse res = mainPageService.getRecentPosts(lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.4 line 26", description = "추천 포스트 조회")
	@GetMapping("/recommend/{lastId}")
	public ResponseEntity<ApiResponse<MainPageResponse>> getRecommend(@RequestHeader("memberId") Long memberId,
																	  @RequestParam("preJob") @Size(max = 5, message = "관심 직군이 5개 이하여야 합니다.") List<Long> preJobs,
																	  @PathVariable @PositiveOrZero(message = "lastId는 0 이상이어야 합니다.") Long lastId) {

		MainPageResponse res = mainPageService.getRecommendPosts(memberId, preJobs, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.4 line 27", description = "팔로잉 포스트 조회")
	@GetMapping("/following/{lastId}")
	public ResponseEntity<ApiResponse<MainPageResponse>> getFollowing(@RequestHeader("memberId") Long memberId,
																   @PathVariable @PositiveOrZero(message = "lastId는 0 이상이어야 합니다.") Long lastId) {

		MainPageResponse res = mainPageService.getFollowingPosts(memberId, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.4 line 28", description = "북마크 포스트 조회")
	@GetMapping("/bookmark/{lastId}")
	public ResponseEntity<ApiResponse<MainPageResponse>> getBookmark(@RequestHeader("memberId") Long memberId,
																	 @PathVariable @PositiveOrZero(message = "lastId는 0 이상이어야 합니다.") Long lastId) {

		MainPageResponse res = mainPageService.getBookmarkedPosts(memberId, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.3 line 35", description = "포스트 검색")
	@GetMapping("/search/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getSearch(@RequestParam(name = "search") String search,
																@PathVariable Long lastId) {

		PostsResponse res = blogService.search(5L, search, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}
}
