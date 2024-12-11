package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostResponse {
    private Long postId;
    private Boolean blinded;
}
