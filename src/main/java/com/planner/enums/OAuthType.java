package com.planner.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthType {

	GOOGLE("google"),
	KAKAO("kakao");
	
	private final String oAuthType;
}
