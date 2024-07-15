package com.planner.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodeStatus {

		CODE_CHECKED("인증됨"),
		CODE_UNCHECKED("인증안됨");
	
	private final String status;
}
