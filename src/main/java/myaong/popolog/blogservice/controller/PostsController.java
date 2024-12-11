package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.request.PostCreateRequest;
import myaong.popolog.blogservice.dto.request.ReportRequest;
import myaong.popolog.blogservice.dto.response.*;
import myaong.popolog.blogservice.service.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/blog/posts")
@RequiredArgsConstructor
public class PostsController {

	private final PostsService postsService;

	@Operation(summary = "API 명세서 v0.4 line 32", description = "블로그 포스트 목록 조회")
	@GetMapping("/members/{memberId}/{lastId}")
	public ResponseEntity<ApiResponse<PostsResponse>> getPostsOf(
			@RequestHeader(value = "memberId", required = false) Long requesterId,
			@PathVariable Long memberId,
			@PathVariable Long lastId
	) {
		PostsResponse res = postsService.getPostsOf(requesterId, memberId, lastId);
		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.4 line 33", description = "포스트 조회")
	@GetMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostDetailResponse>> getPostDetails(
			@PathVariable Long postId,
			@RequestHeader(value = "memberId", required = false) Long memberId) {
		PostDetailResponse res = postsService.getPostDetails(postId, memberId);
		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.4 line 34", description = "URL로 포스트 조회")
	@GetMapping("/{username}/{title}")
	public ResponseEntity<ApiResponse<PostDetailResponse>> getPostByUrl(
			@PathVariable String username,
			@PathVariable String title,
			@RequestHeader(value = "memberId", required = false) Long memberId) {

		PostDetailResponse res = postsService.getPostByUrl(username, title, memberId);
		return ResponseEntity.ok(ApiResponse.onSuccess(res));
	}

	@Operation(summary = "API 명세서 v0.4 line 35", description = "포스트 작성")
	@PostMapping
	public ResponseEntity<ApiResponse<PostCreateResponse>> createPost(
			@RequestBody @Valid PostCreateRequest request,
			@RequestHeader("memberId") Long memberId) {
		PostCreateResponse response = postsService.createPost(memberId, request);
		return ResponseEntity.ok(ApiResponse.onSuccess(response));
	}

	@Operation(summary = "API 명세서 v0.4 line 36", description = "포스트 이미지 등록")
	@PostMapping("/pic")
	public ResponseEntity<ApiResponse<PostPicResponse>> uploadPostImage(
			@RequestHeader("memberId") Long memberId,
			@RequestParam MultipartFile pic) {
		PostPicResponse response = postsService.uploadPostImage(memberId, pic);
		return ResponseEntity.ok(ApiResponse.onSuccess(response));
	}

	@Operation(summary = "API 명세서 v0.4 line 38", description = "포스트 수정")
	@PutMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> updatePost(
			@PathVariable Long postId,
			@RequestBody PostCreateRequest request,
			@RequestHeader("memberId") Long memberId) {
		postsService.updatePost(postId, memberId, request);
		return ResponseEntity.ok(ApiResponse.onSuccess(null));
	}

	@Operation(summary = "API 명세서 v0.4 line 39", description = "포스트 삭제")
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> deletePost(
			@PathVariable Long postId,
			@RequestHeader("memberId") Long memberId) {
		postsService.deletePost(postId, memberId);
		return ResponseEntity.ok(ApiResponse.onSuccess(null));
	}

	@Operation(summary = "API 명세서 v0.3 line 46", description = "좋아요 토글")
	@PutMapping("/{postId}/like")
	public ResponseEntity<ApiResponse<LikeResponse>> toggleLike(
			@PathVariable Long postId,
			@RequestHeader("memberId") Long memberId) {
		LikeResponse response = postsService.toggleLike(postId, memberId);
		return ResponseEntity.ok(ApiResponse.onSuccess(response));
	}

	@Operation(summary = "API 명세서 v0.3 line 47", description = "북마크 토글")
	@PutMapping("/{postId}/bookmark")
	public ResponseEntity<ApiResponse<PostBookmarkResponse>> toggleBookmark(
			@RequestHeader("memberId") Long memberId,
			@PathVariable Long postId) {
		PostBookmarkResponse response = postsService.toggleBookmark(postId, memberId);
		return ResponseEntity.ok(ApiResponse.onSuccess(response));
	}

	@Operation(summary = "API 명세서 v0.4 line 48", description = "콘텐츠 신고")
	@PostMapping("/{postId}/report")
	public ResponseEntity<ApiResponse<Void>> reportPost(
			@PathVariable Long postId,
			@RequestHeader("memberId") Long memberId,
			@RequestBody ReportRequest reportRequest) {
		postsService.reportPost(postId, memberId, reportRequest);
		return ResponseEntity.ok(ApiResponse.onSuccess(null));
	}
}
