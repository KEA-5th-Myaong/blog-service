package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReplyRequest {

    @NotNull(message = "대상 댓글 아이디가 비어 있습니다.")
    private Long commentId;
    @NotBlank(message = "답글 내용을 작성해주세요.")
    private String content;
}