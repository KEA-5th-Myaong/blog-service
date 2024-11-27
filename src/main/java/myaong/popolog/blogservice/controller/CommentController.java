package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.request.CommentPostRequest;
import myaong.popolog.blogservice.dto.request.CommentUpdateRequest;
import myaong.popolog.blogservice.dto.request.ReplyRequest;
import myaong.popolog.blogservice.dto.response.CommentPostResponse;
import myaong.popolog.blogservice.dto.response.CommentUpdateResponse;
import myaong.popolog.blogservice.dto.response.ReplyResponse;
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

    @Operation(summary = "API 명세서 v0.4 line 41", description = "댓글 수정")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateComment(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request) {
        CommentUpdateResponse response = commentService.updateComment(memberId, commentId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "API 명세서 v0.4 line 42", description = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long commentId) {
        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }

    @Operation(summary = "API 명세서 v0.4 line 43", description = "답글 작성")
    @PostMapping("/replies")
    public ResponseEntity<ApiResponse<ReplyResponse>> postReply(
            @RequestHeader("memberId") Long memberId,
            @RequestBody @Valid ReplyRequest request) {
        ReplyResponse response = commentService.postReply(memberId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
    
    @Operation(summary = "API 명세서 v0.4 line 45", description = "답글 삭제")
    @DeleteMapping("replies/{replyId}")
    public ResponseEntity<ApiResponse<Void>> deleteReply(
            @RequestHeader("memberId") Long memberId,
            @PathVariable Long commentId) {
        commentService.deleteReply(memberId, commentId);
        return ResponseEntity.ok(ApiResponse.onSuccess(null));
    }
}
