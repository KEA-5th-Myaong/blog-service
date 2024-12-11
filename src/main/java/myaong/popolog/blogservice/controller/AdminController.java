package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.response.AdminReportedContentsResponse;
import myaong.popolog.blogservice.dto.response.BlindToggleResponse;
import myaong.popolog.blogservice.service.AdminBlogService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/contents")
public class AdminController {

    private final AdminBlogService adminBlogService;

    @Operation(summary = "API 명세서 v0.4 line 96", description = "피신고 콘텐츠 목록 조회")
    @GetMapping("/reported/{lastId}")
    public ApiResponse<AdminReportedContentsResponse> getReportedContents(
            @PathVariable @PositiveOrZero(message = "lastId는 0 이상이어야 합니다.") Long lastId) {
        return ApiResponse.onSuccess(adminBlogService.getReportedContents(lastId));
    }

    @Operation(summary = "API 명세서 v0.4 line 97", description = "블라인드 콘텐츠 목록 조회")
    @GetMapping("/blind/{lastId}")
    public ApiResponse<AdminReportedContentsResponse> getBlindContents(
            @PathVariable @PositiveOrZero(message = "lastId는 0 이상이어야 합니다.") Long lastId) {
        return ApiResponse.onSuccess(adminBlogService.getBlindContents(lastId));
    }

    @Operation(summary = "API 명세서 v0.4 line 98", description = "포스트 블라인드 토글")
    @PutMapping("/posts/{postId}/blind")
    public ApiResponse<BlindToggleResponse> togglePostBlind(@PathVariable Long postId) {
        return ApiResponse.onSuccess(adminBlogService.togglePostBlind(postId));
    }

    @Operation(summary = "API 명세서 v0.4 line 99", description = "댓글/답글 블라인드 토글")
    @PutMapping("/comments/{commentId}/blind")
    public ApiResponse<BlindToggleResponse> toggleCommentBlind(@PathVariable Long commentId) {
        return ApiResponse.onSuccess(adminBlogService.toggleCommentBlind(commentId));
    }

}

