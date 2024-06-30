package com.planner.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.mail.MessagingException;

@RestControllerAdvice
public class RestGlobalExceptionHandler {
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
