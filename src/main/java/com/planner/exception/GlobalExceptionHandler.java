package com.planner.exception;

import java.io.IOException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class) // CustomException 클래스를 value 값으로 설정
	public ModelAndView handleCustomException(CustomException e) { // CustomException 을 매개변수로 받음
		ModelAndView modelAndView = new ModelAndView(); 
		modelAndView.setViewName("redirect:/error");	// 데이터가 남아 뒤로가기 했을때 또다시 에러페이지가뜨는것을 막기위해  redirect 사용
		modelAndView.addObject("errorMessage", e.getErrorCode().getMessage()); // 해당 에러코드 안에 에러메시지를 errorMessage라는 이름의 객체로 컨트롤러에 보내줌
		System.out.println(modelAndView);
		return modelAndView;					// view의 이름과 객체를 리턴 = ModelAndView [view="redirect:/error"; model={errorMessage=해당 에러의 메시지}]																					
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)// 유효성검사에서 발생한 예외 처리 핸들러
	public ModelAndView handleValidErrorException(MethodArgumentNotValidException e) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/error");
		final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE,e.getBindingResult());
		modelAndView.addObject("errorMessage", errorResponse.getMessage());
		return modelAndView;
	}
	
	@ExceptionHandler(IOException.class)// 파일업로드 실패시 발생한 예외 처리 핸들러
	public ModelAndView handleIOException(IOException e) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/error");
		modelAndView.addObject("errorMessage", ErrorCode.FILE_UPLOAD_FAILED);
		return modelAndView; 
		
		
	}
	

}
