package com.planner.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
   import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestGlobalExceptionHandler {
	/**
	 * MailSendException 에러 처리/ 이메일 전송 실패시
	 * @param e (MailSendException) 예외
	 * @return
	 */
	@ExceptionHandler(MailSendException.class)
	public ResponseEntity<ErrorResponse> handleMailSendException(MailSendException e) {
		log.info("메일전송 실패");
		return ErrorResponse.toResponseEntity(ErrorCode.FAIL_SEND_EMAIL);
	}
	
	/**
	 * RestCustomException 커스텀 예외처리
	 * @param e(RestCustomException) 예외
	 * @return
	 */
	@ExceptionHandler(RestCustomException.class)
	public ResponseEntity<ErrorResponse> handleRestCustomException(RestCustomException e){
		
		return ErrorResponse.toResponseEntity(e.getErrorCode());
	}
}
