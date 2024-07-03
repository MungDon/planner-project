package com.planner.exception; 

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	
	ID_DUPLICATE ( HttpStatus.CONFLICT, "이미 사용중인 아이디입니다."),
	NO_ACCOUNT(HttpStatus.BAD_REQUEST,"입력정보가 유효하지않거나 없는계정입니다."),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"입력 정보가 유효하지 않습니다. 사유 : "),
	DB_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"데이터 삭제 실패, 다시 시도해도 안될 시 운영자에게 문의 해주세요"),
	FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 업로드에 실패하였습니다, 다시 시도해도 안될 시 운영자에게 문의 해주세요"),
	WITHDRAWN_MEMBER(HttpStatus.FORBIDDEN,"탈퇴한 회원입니다. 서비스를 이용하시려면 계정을 복구해주세요"),
	ACCOUNT_RESTORE_PENDING(HttpStatus.FORBIDDEN,"회원님의 계정은 복구 대기중입니다."),
	REQUEST_DUPLICATE(HttpStatus.CONFLICT,"이미 신청하였습니다."),
	INELIGIBLE_REQUEST(HttpStatus.BAD_REQUEST,"해당서비스의 대상이 아닙니다."),
	FAIL_CREATE_AUTHCODE(HttpStatus.INTERNAL_SERVER_ERROR,"인증코드 발급이 실패하였습니다."),
	FAIL_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR,"이메일 전송 실패."),
	FAIL_AUTHENTICATION(HttpStatus.BAD_REQUEST,"인증실패"),
	FAIL_CHANGE_PASSWORD(HttpStatus.INTERNAL_SERVER_ERROR,"비밀번호 변경실패, 운영자에게 문의해주세요"),
	SOCIAL_LOGIN_USER(HttpStatus.CONFLICT,"소셜 로그인으로 가입하였습니다. 로그인해주세요.");
	
	private final  HttpStatus status; // http 상태코드
	private final String message;	// 에러 메세지
}
