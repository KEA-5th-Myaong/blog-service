package myaong.popolog.blogservice.dto.response;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Builder
public class PostDetailResponse {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime timestamp;
    private Long memberId;
    private String username;
    private String nickname;
    private String profilePicUrl;
    private int likeCount;
    private boolean isLiked;
    private boolean isBookmarked;
    private int commentCount;
    private List<Comment> comments;

    @Getter
    @Builder
    public static class Comment {
        private String profilePicUrl;
        private Long memberId;
        private String nickname;
        private Long commentId;
        private Long parentCommentId;
        private String comment;
        private LocalDateTime timestamp;
    }
}
