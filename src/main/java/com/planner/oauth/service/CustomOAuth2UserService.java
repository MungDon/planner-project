package com.planner.oauth.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.oauth.user.OAuth2UserInfo;
import com.planner.oauth.user.OAuth2UserInfoFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

	
	@Transactional
	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)throws OAuth2AuthenticationException{
		
		// 유저 정보 가져오기
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
		
		return processOAuth2User(oAuth2UserRequest, oAuth2User);
	}
	
	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest,OAuth2User oAuth2User) {
		// resistrationId 가져오기
		String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

		// accessToken 가져오기
		String accessToken = oAuth2UserRequest.getAccessToken().getTokenValue();
		
		// 각각 OAuth 제공자 별로 주어지는 데이터를 공통의 UserInfo 객체로 변환처리
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId,accessToken,oAuth2User.getAttributes());
	
		OAuth2UserPrincipal oAuth2UserPrincipal = new OAuth2UserPrincipal(oAuth2UserInfo);
		
		 Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2UserPrincipal, null, oAuth2UserPrincipal.getAuthorities());
	     SecurityContextHolder.getContext().setAuthentication(authentication);
	    
	     
		// OAuth2User 인터페이스의 사용자 정의 구현체 클래스 리턴.
		return oAuth2UserPrincipal;
		
	}
}