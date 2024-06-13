package com.planner.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenRedirect {

	LOGIN_SUCCESS_URL("http://localhost:8080/planner/main"),
	LOGIN_FAILED_URL("http://localhost:8080/planner/main/error"),
	LOGOUT_URL("http://localhost:8080/user/logout");
	
	private final String urlText;
}
