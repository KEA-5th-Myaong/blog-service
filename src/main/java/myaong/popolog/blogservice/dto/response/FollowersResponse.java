package myaong.popolog.blogservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FollowersResponse {

	private Long lastId;
	private List<FollowProfileDTO> followedDTOList;
}
