package myaong.popolog.blogservice.dto.request;

import lombok.Getter;

@Getter
public class PostUpdateRequest {
    private String title;
    private String content;
}