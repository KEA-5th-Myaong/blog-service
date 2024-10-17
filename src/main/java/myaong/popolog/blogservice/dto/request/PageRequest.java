package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PageRequest {

	@NotNull
	private Integer lastId;
}
