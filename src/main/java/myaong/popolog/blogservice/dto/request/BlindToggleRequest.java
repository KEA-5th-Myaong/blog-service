package myaong.popolog.blogservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BlindToggleRequest {
    private boolean blinded;
}
