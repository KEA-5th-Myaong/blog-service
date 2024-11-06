package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.service.ProfileCommandService;
import myaong.popolog.blogservice.service.ProfileQueryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Controller("/blog/profile")
public class ProfileController {

	private final ProfileQueryService profileQueryService;
	private final ProfileCommandService profileCommandService;

	@Operation(summary = "API 명세서 v0.3 line 23", description = "팔로우 토글(팔로우 시 알림 발송 기능은 아직 미구현)")
	@PostMapping("/{memberId}/follow")
	public ApiResponse<ProfileResponse.FollowDTO> followMember(@PathVariable(required = false) Long memberId) {
		return ApiResponse.onSuccess(profileCommandService.followMember(memberId));
	}

	@Operation(summary = "API 명세서 v0.3 line 24", description = "팔로잉 조회 (무한 스크롤)")
	@GetMapping("/{memberId}/following/{lastId}")
	public ApiResponse<ProfileResponse.FollowingListDTO> getMemberFollowingList(@PathVariable(required = false) Long memberId, @PathVariable(required = false) Long lastId) {
		return ApiResponse.onSuccess(profileQueryService.getMemberFollowingList(memberId, lastId));
	}

	@Operation(summary = "API 명세서 v0.3 line 25", description = "팔로워 조회 (무한 스크롤)")
	@GetMapping("/{memberId}/followed/{lastId}")
	public ApiResponse<ProfileResponse.FollowedListDTO> getMemberFollowedList(@PathVariable(required = false) Long memberId, @PathVariable(required = false) Long lastId) {
		return ApiResponse.onSuccess(profileQueryService.getMemberFollowedList(memberId, lastId));
	}
}
