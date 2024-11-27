package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.request.CommentPostRequest;
import myaong.popolog.blogservice.dto.response.CommentPostResponse;
import myaong.popolog.blogservice.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "API 명세서 v0.4 line 40", description = "댓글 작성")
    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<CommentPostResponse>> postComment(
            @RequestHeader("memberId") Long memberId,
            @RequestBody @Valid CommentPostRequest request) {
        CommentPostResponse response = commentService.postComment(memberId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
}
