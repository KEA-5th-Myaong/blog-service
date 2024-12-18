package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentPostRequest {

    @NotNull(message = "대상 포스트 아이디가 비어 있습니다.")
    private Long postId;
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;
}
