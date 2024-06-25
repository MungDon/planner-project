package com.planner.exception; 

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	
	ID_DUPLICATE ( HttpStatus.CONFLICT, "이미 사용중인 아이디입니다."),
	NO_ACCOUNT(HttpStatus.NO_CONTENT,"입력정보가 유효하지않거나 없는계정입니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "없는 페이지입니다"),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"입력 정보가 유효하지 않습니다. 사유 : "),
	DB_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"데이터 삭제 실패, 다시 시도해도 안될 시 운영자에게 문의 해주세요"),
	FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 업로드에 실패하였습니다, 다시 시도해도 안될 시 운영자에게 문의 해주세요"),
	WITHDRAWN_MEMBER(HttpStatus.FORBIDDEN,"탈퇴한 회원입니다.");
	
	private final  HttpStatus status;
	private final String message;
}
