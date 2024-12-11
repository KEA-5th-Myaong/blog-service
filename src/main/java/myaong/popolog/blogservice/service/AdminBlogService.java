package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.response.AdminReportedContentsResponse;
import myaong.popolog.blogservice.dto.request.BlindToggleRequest;
import myaong.popolog.blogservice.dto.response.CommentResponse;
import myaong.popolog.blogservice.dto.response.PostResponse;

import java.util.Map;

public interface AdminBlogService {

    AdminReportedContentsResponse getReportedContents(Long lastId);

    AdminReportedContentsResponse getBlindContents(Long lastId);

    BlindToggleRequest toggleCommentBlind(Long commentId);

    BlindToggleRequest togglePostBlind(Long postId); // 반환 타입 변경
}