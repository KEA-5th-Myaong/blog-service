package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.request.NewProfileRequest;
import myaong.popolog.blogservice.dto.response.ProfileInfoResponse;
import myaong.popolog.blogservice.dto.response.ProfileResponse;
import myaong.popolog.blogservice.service.ProfileCommandService;
import myaong.popolog.blogservice.service.ProfileQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/profiles")
public class ProfileController {

	private final ProfileQueryService profileQueryService;
	private final ProfileCommandService profileCommandService;

	@Operation(summary = "서비스간 API v0.4 line 5", description = "블로그 프로필 등록")
	@PostMapping
	public ApiResponse<Object> createProfile(@Valid @RequestBody NewProfileRequest newProfileRequest) {

		profileCommandService.createProfile(newProfileRequest);

		return ApiResponse.onSuccess(null);
	}

	@Operation(summary = "서비스간 API v0.4 line 6", description = "회원의 프로필 정보 조회")
	@GetMapping(headers = "memberId")
	public ApiResponse<ProfileResponse> getProfile(@RequestHeader(name = "memberId") Long memberId) {

		ProfileResponse res = profileQueryService.getProfileByMemberId(memberId);

		return ApiResponse.onSuccess(res);
	}

	@Operation(summary = "API 명세서 v0.4 line 31", description = "회원 정보 조회 (블로그 접속 시)")
	@GetMapping("/{memberId}/info")
	public ApiResponse<ProfileInfoResponse> getProfileInfo(@RequestHeader(name = "memberId", required = false) Long requesterId,
														   @PathVariable("memberId") Long memberId) {

		ProfileInfoResponse res = profileQueryService.getProfileInfo(requesterId, memberId);

		return ApiResponse.onSuccess(res);
	}

	@Operation(summary = "API 명세서 v0.4 line 49", description = "타인 정보 조회")
	@GetMapping(params = "username")
	public ApiResponse<ProfileResponse> getProfile(@RequestParam(name = "username") String username) {

		ProfileResponse res = profileQueryService.getProfileByUsername(username);

		return ApiResponse.onSuccess(res);
	}

	@Operation(summary = "API 명세서 v0.3 line 23", description = "팔로우 토글(팔로우 시 알림 발송 기능은 아직 미구현)")
	@PostMapping("/{memberId}/follow")
	public ApiResponse<ProfileResponse.FollowDTO> followProfile(@PathVariable(required = false) Long memberId) {
		return ApiResponse.onSuccess(profileCommandService.followProfile(memberId));
	}

	@Operation(summary = "API 명세서 v0.3 line 24", description = "팔로잉 조회 (무한 스크롤)")
	@GetMapping("/{memberId}/following/{lastId}")
	public ApiResponse<ProfileResponse.FollowingListDTO> getProfileFollowingList(@PathVariable(required = false) Long memberId, @PathVariable(required = false) Long lastId) {
		return ApiResponse.onSuccess(profileQueryService.getProfileFollowingList(memberId, lastId));
	}

	@Operation(summary = "API 명세서 v0.3 line 25", description = "팔로워 조회 (무한 스크롤)")
	@GetMapping("/{memberId}/followed/{lastId}")
	public ApiResponse<ProfileResponse.FollowedListDTO> getProfileFollowedList(@PathVariable(required = false) Long memberId, @PathVariable(required = false) Long lastId) {
		return ApiResponse.onSuccess(profileQueryService.getProfileFollowedList(memberId, lastId));
	}
}
