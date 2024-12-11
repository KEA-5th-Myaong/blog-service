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
    private Boolean blinded;


    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .profilePicUrl(comment.getProfile() != null ? comment.getProfile().getProfilePicUrl() : null)
                .memberId(comment.getProfile() != null ? comment.getProfile().getId() : null)
                .nickname(comment.getProfile() != null ? comment.getProfile().getNickname() : null)
                .commentId(comment.getId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .comment(comment.getContent())
                .timestamp(comment.getCreatedAt())
                .blinded(comment.getIsBlinded())
                .build();
    }
}
