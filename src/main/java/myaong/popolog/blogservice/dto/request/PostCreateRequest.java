package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostCreateRequest {
    @NotBlank(message = "포스트 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "포스트 내용을 입력해주세요.")
    private String content;
}