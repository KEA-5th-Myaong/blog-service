package myaong.popolog.blogservice.controller;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.response.PostsResponse;
import myaong.popolog.blogservice.service.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/posts")
@RequiredArgsConstructor
public class PostsController {

	private final PostsService postsService;

	@GetMapping("/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getRecent(@RequestParam(name = "search", required = false) String search,
																@PathVariable Long lastId) {

		PostsResponse res;
		if (search == null)
			res = postsService.getRecent(5L, lastId);
		else
			res = postsService.search(5L, search, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@GetMapping("/members/{memberId}/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getPostsOf(@PathVariable String memberId,
																 @PathVariable Long lastId) {

		PostsResponse res = postsService.getPostsOf(5L, lastId);

		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}
}
