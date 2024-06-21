package com.planner.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{ // 해당 Exception은 RuntimeException을 상속받기 때문에 RunTimeException취급.
	
	ErrorCode errorCode; // 에러코드를 설정

}