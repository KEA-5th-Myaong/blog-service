package myaong.popolog.blogservice.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiCode {

	OK(HttpStatus.OK, "COMMON_2000", "OK"),
	INVALID_DATA(HttpStatus.BAD_REQUEST, "COMMON_4000", "Request data missing or invalid"),
	READ_ONLY_ACCESS(HttpStatus.FORBIDDEN, "COMMON_4031", "You can only read"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_4050", "Method not allowed"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_5000", "Internal Server Error"),
	DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_5001", "DB Error"),

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "PROFILE_4040", "존재하지 않는 회원입니다."),
	SELF_FOLLOW_CONFLICT(HttpStatus.CONFLICT, "PROFILE_4090", "Cannot follow oneself."),
	MEMBER_CONFLICT(HttpStatus.CONFLICT, "PROFILE_4091", "이미 프로필이 존재하는 회원입니다."),
	UNSUPPORTED_PROFILE_PIC(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "PROFILE_4150", "Unsupported media type"),

	INVALID_PREJOBS(HttpStatus.BAD_REQUEST, "POST_4000", "회원의 관심 직군에 한해서만 요청할 수 있습니다."),
	READ_ONLY_ACCESS_POST(HttpStatus.FORBIDDEN, "POST_4030", "You can only read this post"),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_4040", "존재하지 않는 포스트입니다."),
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "POST_4150", "Unsupported Media Type"),

	READ_ONLY_ACCESS_COMMENT(HttpStatus.FORBIDDEN, "COMMENT_4030", "You can only read this comment"),
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_4040", "존재하지 않는 댓글입니다."),

	REPORT_DUPLICATED(HttpStatus.CONFLICT, "REPORT_4090", "이미 신고한 콘텐츠입니다."),
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
