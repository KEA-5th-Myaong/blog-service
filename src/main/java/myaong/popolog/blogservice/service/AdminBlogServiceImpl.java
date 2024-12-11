package myaong.popolog.blogservice.service;

import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiCode;
import myaong.popolog.blogservice.common.exception.ApiException;
import myaong.popolog.blogservice.dto.response.*;
import myaong.popolog.blogservice.entity.Comment;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Report;
import myaong.popolog.blogservice.enums.ContentsType;
import myaong.popolog.blogservice.repository.CommentRepository;
import myaong.popolog.blogservice.repository.PostRepository;
import myaong.popolog.blogservice.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminBlogServiceImpl implements AdminBlogService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public AdminReportedContentsResponse getReportedContents(Long lastId) {
        // Step 1: Fetch reports
        List<Report> reports = reportRepository.findByLastId(lastId);

        // Step 2: Fetch posts and comments by IDs
        List<Long> postIds = reports.stream()
                .filter(r -> r.getContentsType() == ContentsType.POST)
                .map(Report::getContentsId)
                .collect(Collectors.toList());

        List<Long> commentIds = reports.stream()
                .filter(r -> r.getContentsType() == ContentsType.COMMENT)
                .map(Report::getContentsId)
                .collect(Collectors.toList());

        List<Post> posts = postIds.isEmpty() ? List.of() : postRepository.findPostsByIds(postIds);
        List<Comment> comments = commentIds.isEmpty() ? List.of() : commentRepository.findByIdIn(commentIds);

        // Step 3: Fetch report counts
        Map<Long, Integer> reportCounts = reportRepository.countReportsByContents().stream()
                .collect(Collectors.toMap(o -> (Long) o[0], o -> ((Long) o[1]).intValue()));

        // Step 4: Return combined response
        return AdminReportedContentsResponse.fromReports(reports, posts, comments, reportCounts);
    }


    @Transactional(readOnly = true)
    @Override
    public AdminReportedContentsResponse getBlindContents(Long lastId) {
        // 블라인드된 게시물만 조회
        List<Post> posts = (lastId == 0)
                ? postRepository.findByIsBlindedTrueOrderByIdDesc()
                : postRepository.findByIsBlindedTrueAndIdLessThanOrderByIdDesc(lastId);

        // 블라인드 콘텐츠 응답 생성
        return AdminReportedContentsResponse.fromBlindedContents(posts);
    }

    @Transactional
    @Override
    public BlindToggleResponse toggleCommentBlind(Long commentId) { // 이름 수정
        // 필요한 데이터만 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(ApiCode.COMMENT_NOT_FOUND));

        // 상태 반전
        comment.toggleBlind();

        // Custom Query로 필요한 필드만 업데이트
        commentRepository.updateBlindedStatus(comment.getId(), comment.getIsBlinded());

        // 응답 생성
        return BlindToggleResponse.builder() // 이름 수정
                .blinded(comment.getIsBlinded())
                .build();
    }


    @Transactional
    @Override
    public BlindToggleResponse togglePostBlind(Long postId) { // 이름 수정
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ApiCode.POST_NOT_FOUND));

        post.toggleBlind();
        postRepository.updateBlindedStatus(post.getId(), post.getIsBlinded());

        return BlindToggleResponse.builder() // 이름 수정
                .blinded(post.getIsBlinded())
                .build();
    }

}
