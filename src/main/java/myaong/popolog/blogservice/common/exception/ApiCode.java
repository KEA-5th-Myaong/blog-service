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

	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_4040", "존재하지 않는 포스트입니다."),
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "POST_4150", "Unsupported Media Type"),

	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_4040", "존재하지 않는 댓글입니다."),

	MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE_4040", "존재하지 않는 메시지입니다."),
	MESSAGE_STATE_CONFLICT(HttpStatus.CONFLICT, "MESSAGE_4090", "Message already replied, cannot edit."),

	REPORT_DUPLICATED(HttpStatus.CONFLICT, "REPORT_4090", "이미 신고한 콘텐츠입니다."),
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
