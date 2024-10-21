package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import myaong.popolog.blogservice.entity.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private String profilePicUrl;
    private Long memberId;
    private String nickname;
    private Long commentId;
    private Long parentCommentId;
    private String comment;
    private LocalDateTime timestamp;

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .profilePicUrl(comment.getMemberProfile() != null ? comment.getMemberProfile().getProfilePicUrl() : null)
                .memberId(comment.getMemberProfile() != null ? comment.getMemberProfile().getId() : null)
                .nickname(comment.getMemberProfile() != null ? comment.getMemberProfile().getNickname() : null)
                .commentId(comment.getId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .comment(comment.getContent())
                .timestamp(comment.getCreatedAt())
                .build();
    }
}
