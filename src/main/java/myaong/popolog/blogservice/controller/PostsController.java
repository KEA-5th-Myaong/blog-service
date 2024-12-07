package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.response.LikeResponse;
import myaong.popolog.blogservice.dto.response.PostDetailResponse;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.service.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/posts")
@RequiredArgsConstructor
public class PostsController {

	private final PostsService postsService;

	@Operation(summary = "API 명세서 v0.4 line 32", description = "블로그 포스트 목록 조회")
	@GetMapping("/members/{memberId}/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getPostsOf(
			@PathVariable Long memberId,
			@PathVariable Long lastId
	) {
		PostsResponse res = postsService.getPostsOf(memberId, lastId);
		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.4 line 33", description = "포스트 조회")
	@GetMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostDetailResponse>> getPostDetails(
			@PathVariable Long postId,
			@RequestHeader(value = "memberId") Long memberId) {
		PostDetailResponse res = postsService.getPostDetails(postId, memberId);
		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.4 line 34", description = "URL로 포스트 조회")
	@GetMapping("/{username}/{title}")
	public ResponseEntity<ApiResponse<PostDetailResponse>> getPostByUrl(
			@PathVariable String username,
			@PathVariable String title,
			@RequestHeader(value = "memberId") Long memberId) {

		PostDetailResponse response = postsService.getPostByUrl(username, title, memberId);
		return ResponseEntity.ok(ApiResponse.onSuccess(response));
	}

	@Operation(summary = "API 명세서 v0.3 line 46", description = "좋아요 토글")
	@PutMapping("/{postId}/like")
	public ResponseEntity<ApiResponse<LikeResponse>> toggleLike(
			@PathVariable Long postId,
			@RequestHeader("memberId") Long memberId) {
		LikeResponse response = postsService.toggleLike(postId, memberId);
		return ResponseEntity.ok(ApiResponse.onSuccess(response));
	}
}
