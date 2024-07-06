package com.planner.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

		ASYNC_EXCEPTION("비동기 예외처리"),
		SYNC_EXCEPTION("동기 예외처리");
	
	private final String errorType; 
}
