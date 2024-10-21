package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
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

	@Operation(summary = "API 명세서 v0.3 line 36", description = "블로그 포스트 목록 조회")
	@GetMapping("/members/{memberId}/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getPostsOf(@PathVariable String memberId,
																 @PathVariable Long lastId) {

		PostsResponse res = postsService.getPostsOf(5L, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.3 line 37", description = "포스트 조회")
	@GetMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostDetailResponse>> getPostById(@PathVariable Long postId) {
		PostDetailResponse res = postsService.getPostById(postId);
		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}
}
