package myaong.popolog.blogservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class PrejobsRequest {

	@NotNull
	@Positive(message = "잘못된 동기화 값이 전달되었습니다.")
	Long memberPrejobId;
	@Positive(message = "잘못된 동기화 값이 전달되었습니다.")
	Long jobId;
	@NotBlank(message = "잘못된 동기화 값이 전달되었습니다.")
	String jobName;
}
