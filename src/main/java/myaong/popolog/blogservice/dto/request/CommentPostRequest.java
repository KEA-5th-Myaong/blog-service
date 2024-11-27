package myaong.popolog.blogservice.dto.request;

import lombok.Getter;

@Getter
public class CommentPostRequest {
    private Integer postId;
    private String content;
}
