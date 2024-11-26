package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FollowingsResponse {

	private Long lastId;
	private List<FollowProfileDTO> followingDTOList;
}
