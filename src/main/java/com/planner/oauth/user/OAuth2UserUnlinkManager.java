package com.planner.oauth.user;

import org.springframework.stereotype.Component;

import com.planner.oauth.exception.OAuth2AuthenticationProcessingException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2UserUnlinkManager {

	private final GoogleOAuth2UserUnlink googleOAuth2UserUnlink;
	
	public void unlink(OAuth2Provider provider, String accessToken)  {
		if(OAuth2Provider.GOOGLE.equals(provider)) {
			googleOAuth2UserUnlink.unlink(accessToken);
		} else {
			throw new OAuth2AuthenticationProcessingException(
                    "해당 : " + provider.getRegistrationId() + " 서비스는 지원하지 않는 로그아웃입니다.");
        }
	}
}
