package com.planner.oauth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.planner.mapper.MemberMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	 private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
	 private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
	 private final MemberMapper memberMapper;
	 
	 @Override
	 public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		 
		 String targetUrl;
		 
		 targetUrl = determineTargetUrl(request, response, authentication);
		 
		 clearAuthenticationAttributes(request,response);
		 getRedirectStrategy().sendRedirect(request, response, targetUrl);
	 }
	 
	 @Transactional
	 protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
		  
		 String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NANE)
				 				.map(cookie::getValue)
				 				.orElse("");
	 }
}
