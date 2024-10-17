package myaong.popolog.blogservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.request.PageRequest;
import myaong.popolog.blogservice.dto.request.RecommendRequest;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

	private final BlogService blogService;

	@GetMapping("/recommend")
	public ResponseEntity<ApiResponse<PostsResponse>> getRecommend(@Valid @RequestBody RecommendRequest request) {

		PostsResponse res = blogService.getRecommend(5L, request);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@GetMapping("/following")
	public ResponseEntity<ApiResponse<PostsResponse>> getFollowing(@Valid @RequestBody PageRequest request) {

		PostsResponse res = blogService.getFollowing(5L, request);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@GetMapping("/bookmark")
	public ResponseEntity<ApiResponse<PostsResponse>> getBookmark(@Valid @RequestBody PageRequest request) {

		PostsResponse res = blogService.getBookmark(5L, request);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}
}
