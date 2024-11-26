package myaong.popolog.blogservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import myaong.popolog.blogservice.common.exception.ApiResponse;
import myaong.popolog.blogservice.dto.request.NewProfileRequest;
import myaong.popolog.blogservice.dto.request.UpdateProfileRequest;
import myaong.popolog.blogservice.dto.response.*;
import myaong.popolog.blogservice.service.ProfileCommandService;
import myaong.popolog.blogservice.service.ProfileQueryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/profiles")
public class ProfileController {

	private final ProfileQueryService profileQueryService;
	private final ProfileCommandService profileCommandService;

	@Operation(summary = "서비스간 API v0.4 line 5", description = "블로그 프로필 등록")
	@PostMapping
	public Object createProfile(@Valid @RequestBody NewProfileRequest newProfileRequest) {

		profileCommandService.createProfile(newProfileRequest);
		return null;
	}

	@Operation(summary = "서비스간 API v0.4 line 6", description = "회원의 프로필 정보 조회")
	@GetMapping(headers = "memberId")
	public ProfileResponse getProfile(@RequestHeader(name = "memberId") Long memberId) {

		return profileQueryService.getProfileByMemberId(memberId);
	}

	@Operation(summary = "API 명세서 v0.4 line 31", description = "회원 정보 조회 (블로그 접속 시)")
	@GetMapping("/{memberId}/info")
	public ApiResponse<ProfileInfoResponse> getProfileInfo(@RequestHeader(name = "memberId", required = false) Long requesterId,
														   @PathVariable("memberId") @NotNull(message = "회원 아이디가 비어있을 수 없습니다.") Long memberId) {

		return ApiResponse.onSuccess(profileQueryService.getProfileInfo(requesterId, memberId));
	}

	@Operation(summary = "API 명세서 v0.4 line 49", description = "타인 정보 조회")
	@GetMapping(params = "username")
	public ApiResponse<ProfileResponse> getProfile(@RequestParam(name = "username") String username) {

		return ApiResponse.onSuccess(profileQueryService.getProfileByUsername(username));
	}

	@Operation(summary = "API 명세서 v0.4 line 53", description = "프로필 사진 수정")
	@PostMapping("/pic")
	public ApiResponse<ProfilePicUrlResponse> updateProfilePic(@RequestHeader("memberId") Long memberId,
															   @RequestParam(value = "pic", required = false) MultipartFile pic) {

		if (pic == null || pic.isEmpty()) {
			return ApiResponse.onSuccess(profileCommandService.updateProfilePic(memberId));
		} else {
			return ApiResponse.onSuccess(profileCommandService.updateProfilePic(memberId, pic));
		}
	}

	@Operation(summary = "API 명세서 v0.4 line 54", description = "프로필 정보 수정")
	@PutMapping
	public ApiResponse<Object> updateProfile(@RequestHeader("memberId") Long memberId,
											 @Valid @RequestBody UpdateProfileRequest req) {

		profileCommandService.updateProfile(memberId, req);
		return ApiResponse.onSuccess(null);
	}

	@Operation(summary = "API 명세서 v0.4 line 50", description = "팔로우 토글")
	@PostMapping("/{memberId}/follow")
	public ApiResponse<FollowResponse> followProfile(@RequestHeader("memberId") Long requesterId,
													 @PathVariable @NotNull(message = "회원 아이디가 비어있을 수 없습니다.") Long memberId) {

		return ApiResponse.onSuccess(profileCommandService.followProfile(requesterId, memberId));
	}

	@Operation(summary = "API 명세서 v0.4 line 51", description = "팔로잉 조회")
	@GetMapping("/{memberId}/following/{lastId}")
	public ApiResponse<FollowingsResponse> getProfileFollowingList(@RequestHeader(name = "memberId", required = false) Long requesterId,
																   @PathVariable @NotNull(message = "회원 아이디가 비어있을 수 없습니다.") Long memberId,
																   @PathVariable @PositiveOrZero(message = "lastId는 0 이상이어야 합니다.") Long lastId) {

		return ApiResponse.onSuccess(profileQueryService.getProfileFollowingList(requesterId, memberId, lastId));
	}

	@Operation(summary = "API 명세서 v0.4 line 52", description = "팔로워 조회")
	@GetMapping("/{memberId}/followed/{lastId}")
	public ApiResponse<FollowersResponse> getProfileFollowedList(@RequestHeader(name = "memberId", required = false) Long requesterId,
																 @PathVariable @Positive(message = "잘못된 회원 아이디입니다.") Long memberId,
																 @PathVariable @PositiveOrZero(message = "lastId는 0 이상이어야 합니다.") Long lastId) {

		return ApiResponse.onSuccess(profileQueryService.getProfileFollowedList(requesterId, memberId, lastId));
	}
}
