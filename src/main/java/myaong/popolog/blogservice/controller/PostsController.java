package myaong.popolog.blogservice.controller;

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

	@GetMapping("/members/{memberId}/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getPostsOf(@PathVariable String memberId,
																 @PathVariable Long lastId) {

		PostsResponse res = postsService.getPostsOf(5L, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@GetMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostDetailResponse>> getPostById(@PathVariable Long postId) {
		PostDetailResponse res = postsService.getPostById(postId);
		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}
}
