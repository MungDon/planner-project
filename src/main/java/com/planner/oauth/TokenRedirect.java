package com.planner.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenRedirect {

	LOGIN_SUCCESS_URL("http://localhost:8080/member/auth"),
	LOGIN_FAILED_URL("http://localhost:8080/error"),
	LOGOUT_URL("http://localhost:8080/member/auth/signout");
	
	private final String urlText;
}
