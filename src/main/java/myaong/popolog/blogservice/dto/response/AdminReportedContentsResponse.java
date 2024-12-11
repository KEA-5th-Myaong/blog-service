package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import myaong.popolog.blogservice.entity.Comment;
import myaong.popolog.blogservice.entity.Post;
import myaong.popolog.blogservice.entity.Report;
import myaong.popolog.blogservice.enums.ContentsType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class AdminReportedContentsResponse {

    private Long lastId;
    private List<ReportedContent> contents;

    @Getter
    @Builder
    public static class ReportedContent {
        private Long postId;
        private String title; // 제목 (포스트의 경우)
        private String contentsType; // POST or COMMENT
        private Long contentsId; // 콘텐츠 ID
        private String content; // 내용
        private Integer reportCount; // 신고 횟수
    }

    public static AdminReportedContentsResponse fromReports(List<Report> reports, List<Post> posts, List<Comment> comments, Map<Long, Integer> reportCounts) {
        Map<Long, Post> postMap = posts.stream().collect(Collectors.toMap(Post::getId, p -> p));
        Map<Long, Comment> commentMap = comments.stream().collect(Collectors.toMap(Comment::getId, c -> c));

        return AdminReportedContentsResponse.builder()
                .lastId(reports.isEmpty() ? -1L : reports.get(reports.size() - 1).getId())
                .contents(reports.stream()
                        .map(r -> {
                            if (r.getContentsType() == ContentsType.POST) {
                                Post post = postMap.get(r.getContentsId());
                                return ReportedContent.builder()
                                        .postId(post.getId())
                                        .title(post.getTitle())
                                        .contentsType("POST")
                                        .contentsId(r.getContentsId())
                                        .content(post.getContent())
                                        .reportCount(reportCounts.getOrDefault(r.getContentsId(), 0))
                                        .build();
                            } else if (r.getContentsType() == ContentsType.COMMENT) {
                                Comment comment = commentMap.get(r.getContentsId());
                                return ReportedContent.builder()
                                        .postId(comment.getPost().getId()) // Post 객체에서 ID 추출
                                        .title(null)
                                        .contentsType("COMMENT")
                                        .contentsId(r.getContentsId())
                                        .content(comment.getContent())
                                        .reportCount(reportCounts.getOrDefault(r.getContentsId(), 0))
                                        .build();
                            }
                            return null;
                        })
                        .filter(c -> c != null) // null 값 필터링
                        .collect(Collectors.toList()))
                .build();
    }


    public static AdminReportedContentsResponse fromBlindedContents(List<Post> posts) {
        return AdminReportedContentsResponse.builder()
                .lastId(posts.isEmpty() ? -1L : posts.get(posts.size() - 1).getId())
                .contents(posts.stream()
                        .map(post -> ReportedContent.builder()
                                .postId(post.getId())
                                .title(post.getTitle())
                                .contentsType("POST")
                                .contentsId(post.getId())
                                .content(post.getContent())
                                .reportCount(0) // 신고 횟수는 블라인드 콘텐츠에 포함되지 않음
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
