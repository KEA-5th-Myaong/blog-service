package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReplyRequest {
    private Long commentId;
    @NotEmpty(message = "내용은 비어 있을 수 없습니다.")
    private String content;
}