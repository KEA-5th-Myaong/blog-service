package myaong.popolog.blogservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.request.PageRequest;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.service.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/posts")
@RequiredArgsConstructor
public class PostsController {

	private final PostsService postsService;

	@GetMapping
	public ResponseEntity<ApiResponse<PostsResponse>> getRecent(@RequestParam(name = "search", required = false) String search,
																@Valid @RequestBody PageRequest request) {

		PostsResponse res;
		if (search == null)
			res = postsService.getRecent(5L, request);
		else
			res = postsService.search(5L, search, request);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@GetMapping("/members/{memberId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getPostsOf(@PathVariable String memberId,
																 @Valid @RequestBody PageRequest request) {

		PostsResponse res = postsService.getPostsOf(5L, request);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}
}
