package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.dto.response.AdminReportedContentsResponse;
import myaong.popolog.blogservice.dto.response.BlindToggleResponse;

public interface AdminBlogService {

    AdminReportedContentsResponse getReportedContents(Long lastId);

    AdminReportedContentsResponse getBlindContents(Long lastId);

    BlindToggleResponse toggleCommentBlind(Long commentId);

    BlindToggleResponse togglePostBlind(Long postId); // 반환 타입 변경
}