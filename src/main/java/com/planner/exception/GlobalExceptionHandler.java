package com.planner.exception;

import java.io.IOException;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	
	/**
	 * 강제 예외 발생시킨 CustomException 처리
	 * @param e ( 발생한 예외 종류가 들어감)
	 * @param model	
	 * @return
	 */
	@ExceptionHandler(CustomException.class) // CustomException 클래스를 value 값으로 설정
	public String handleCustomException(CustomException e,Model model) { 
		log.info("여기옴?에러"+e.getErrorCode().getMessage());
		model.addAttribute("errorMessage", e.getErrorCode().getMessage());
		model.addAttribute("deleteMember",ErrorCode.WITHDRAWN_MEMBER.getMessage());
		return "/error/throws_error";
	}
	
	/**
	 *  @Valid 어노테이션으로 객체 안에 유효성검사(@NotNull, @NotEmpty ) 와같은MethodArgumentNotValidException 예외 발생시 처리
	 * @param e (유효성검사에서 걸린 예외가 들어감)
	 * @param model
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)// 유효성검사에서 발생한 예외 처리 핸들러
	public String handleValidErrorException(MethodArgumentNotValidException e,Model model) {
		final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE,e.getBindingResult());
		model.addAttribute("errorMessage", errorResponse.getMessage());
		return "/error/throws_error";
	}
	
	/**
	 *  IOException 에러 처리 보통 파일업로드 부분
	 * @param e (IOException) 예외가 들어감
	 * @param model
	 * @return
	 */
	@ExceptionHandler(IOException.class)// 파일업로드 실패시 발생한 예외 처리 핸들러
	public String handleIOException(IOException e,Model model) {
		model.addAttribute("errorMessage", ErrorCode.FILE_UPLOAD_FAILED.getMessage());
		return "/error/throws_error";
	}
	
	/**
	 * MessagingException 에러 처리/ 이메일 전송 실패시
	 * @param e (MessagingException) 예외
	 * @param model
	 * @return
	 */
	@ExceptionHandler(MessagingException.class)
	public String handleMessagingException(MessagingException e, Model model) {
		model.addAttribute("errorMessage",ErrorCode.FAIL_SEND_EMAIL.getMessage());
		return "/error/throws_error";
	}
}
