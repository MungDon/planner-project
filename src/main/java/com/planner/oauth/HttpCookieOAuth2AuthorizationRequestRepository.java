package com.planner.oauth;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// OAuth2 인증 과정중 state,Redirect_URI등의 파라미터를 어딘가에 저장해야하는데 이를 쿠키에 저장하는 방식으로 구현한 사용자 정의 클래스
// 스프링 빈으로 등록하고 SecurityConfig에서 AuthrizationRequestRepository로 설정

// 스프링 시큐리티 OAuth2 관련 필터인 OAuth2AuthrizationRequestRedirectFilter와
// OAuth2LoginAuthenticationFilter에서 인증과정중에 호출
@RequiredArgsConstructor
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository 
			implements AuthorizationRequestRepository<OAuth2AuthorizationRequest>{
	
	public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	public static final String 	REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
	public static final String MODE_PARAM_COOKIE_NAME ="mode";
	public static final int COOKIE_EXPIRE_SECONDS = 180;
	
	

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
									.map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
									.orElse(null);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
			HttpServletResponse response) {
			if(authorizationRequest == null) {
				removeAuthorizationRequestCookies(request,response);
				return;
			}
			
			CookieUtils.addCookie(response, 
										OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
										CookieUtils.serialize(authorizationRequest),
										COOKIE_EXPIRE_SECONDS);
			
			String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
			if(StringUtils.hasText(redirectUriAfterLogin)) {
				CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME,
												redirectUriAfterLogin, 
												COOKIE_EXPIRE_SECONDS);
			}
			
			String mode = request.getParameter(MODE_PARAM_COOKIE_NAME);
			if(StringUtils.hasText(mode)) {
				CookieUtils.addCookie(response, 
													MODE_PARAM_COOKIE_NAME,
													mode, 
													COOKIE_EXPIRE_SECONDS);
			}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return this.loadAuthorizationRequest(request);
	}
	
	public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
		CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
		CookieUtils.deleteCookie(request, response, MODE_PARAM_COOKIE_NAME);
	}

}
