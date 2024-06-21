package com.planner.oauth.user;

import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuth2UserInfoFactory {
	
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, String accessToken, Map<String,Object> attribute ) {
		if(OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
			return new GoogleOAuth2UserInfo(accessToken,attribute);
		}else if(OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)){
			return new KakaoOAuth2UserInfo(accessToken,attribute);
		} else {
			throw new OAuth2AuthenticationException("지원하지않는 로그인방식입니다."); 
		}
	}
}
