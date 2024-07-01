package com.planner.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestCustomException extends RuntimeException{

	private final ErrorCode errorCode;
}
