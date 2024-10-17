package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class RecommendRequest {

	@NotNull
	private Integer lastId;
	@NotNull	// 빈 배열 가능
	private List<Integer> preJob;
}
