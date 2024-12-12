package myaong.popolog.blogservice.dto.response;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class PostBookmarkResponse {
    private boolean bookmark;
}
