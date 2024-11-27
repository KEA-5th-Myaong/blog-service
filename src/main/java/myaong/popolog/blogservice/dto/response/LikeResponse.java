package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponse {
    private boolean like;
}
