package myaong.popolog.blogservice.dto.request;

import lombok.Getter;
import myaong.popolog.blogservice.enums.ContentsType;

@Getter
public class ReportRequest {
    private Long contentId;
    private ContentsType contentType;
}
