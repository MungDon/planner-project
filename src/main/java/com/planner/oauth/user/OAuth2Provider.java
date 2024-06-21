package com.planner.oauth.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
	GOOGLE("google"),
	KAKAO("kakao");
	//TODO - 추후 카카오 추가
	private final String registrationId;
}
