package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import myaong.popolog.blogservice.common.annotation.Enum;
import myaong.popolog.blogservice.enums.ContentsType;

@Getter
public class ReportRequest {

    @NotNull(message = "대상 콘텐츠 아이디가 비어 있습니다.")
    private Long contentId;
    @Enum(enumClass = ContentsType.class, message = "콘텐츠 타입이 올바르지 않습니다. 다음 값만 사용 가능합니다: POST, COMMENT")
    private String contentType;
}
