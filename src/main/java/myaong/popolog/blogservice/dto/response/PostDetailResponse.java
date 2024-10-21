package myaong.popolog.blogservice.dto.response;
import lombok.Builder;
import lombok.Getter;
import myaong.popolog.blogservice.entity.Comment;
import myaong.popolog.blogservice.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostDetailResponse {

    private String title;
    private String content;
    private LocalDateTime timestamp;
    private Long memberId;
    private String nickname;
    private String profilePicUrl;
    private int likeCount;
    private boolean isBookmarked;
    private int commentCount;
    private List<CommentResponse> comments;

    public static PostDetailResponse of(Post post, List<Comment> comments, boolean isBookmarked) {
        return PostDetailResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .timestamp(post.getCreatedAt())
                .memberId(post.getMemberProfile() != null ? post.getMemberProfile().getId() : null)
                .nickname(post.getMemberProfile() != null ? post.getMemberProfile().getNickname() : null)
                .profilePicUrl(post.getMemberProfile() != null ? post.getMemberProfile().getProfilePicUrl() : null)
                .likeCount(post.getLikes().size())
                .isBookmarked(isBookmarked)
                .commentCount(comments.size())
                .comments(comments.stream().map(CommentResponse::of).toList())
                .build();
    }
}
