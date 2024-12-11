package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.dto.response.AdminReportedContentsResponse;
import myaong.popolog.blogservice.dto.request.BlindToggleRequest;
import myaong.popolog.blogservice.dto.response.CommentResponse;
import myaong.popolog.blogservice.dto.response.PostResponse;
import myaong.popolog.blogservice.service.AdminBlogService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/contents")
public class AdminController {

    private final AdminBlogService adminBlogService;

    @Operation(summary = "API 명세서 v0.4 line 96", description = "피신고 콘텐츠 목록 조회")
    @GetMapping("/reported/{lastId}")
    public AdminReportedContentsResponse getReportedContents(@PathVariable Long lastId) {
        if (lastId < 0) {
            throw new ApiException(ApiCode.INVALID_DATA);
        }
        return adminBlogService.getReportedContents(lastId);
    }
    @Operation(summary = "API 명세서 v0.4 line 97", description = "블라인드 콘텐츠 목록 조회")
    @GetMapping("/blind/{lastId}")
    public AdminReportedContentsResponse getBlindContents(@PathVariable Long lastId) {
        if (lastId < 0) {
            throw new ApiException(ApiCode.INVALID_DATA);
        }
        return adminBlogService.getBlindContents(lastId);
    }

    @Operation(summary = "API 명세서 v0.4 line 98", description = "포스트 블라인드 토글")
    @PutMapping("/posts/{postId}/blind")
    public BlindToggleRequest togglePostBlind(@PathVariable Long postId) {
        return adminBlogService.togglePostBlind(postId);
    }

    @Operation(summary = "API 명세서 v0.4 line 99", description = "댓글/답글 블라인드 토글")
    @PutMapping("/comments/{commentId}/blind")
    public BlindToggleRequest toggleCommentBlind(@PathVariable Long commentId) {
        return adminBlogService.toggleCommentBlind(commentId);
    }
}

