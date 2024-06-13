package com.planner.oauth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.planner.dto.response.member.ResOAuth2MemberLogin;
import com.planner.oauth.CookieUtils;
import com.planner.oauth.TokenRedirect;
import com.planner.oauth.service.OAuth2Service;
import com.planner.oauth.service.OAuth2UserPrincipal;
import com.planner.oauth.user.OAuth2Provider;
import com.planner.oauth.user.OAuth2UserUnlinkManager;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	 private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
	 private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
	 private final OAuth2Service oAuth2Service;
	 
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
				 				.map(Cookie::getValue)
				 				.orElse("");
		 
		 OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);
		 if(principal == null) {
			 logger.debug("유저 인증 정보가 없습니다.");
			 return UriComponentsBuilder.fromUriString(TokenRedirect.LOGIN_FAILED_URL.getUrlText())
					 									   .queryParam("error", "로그인 정보를 찾을 수 없습니다.")
					 									   .build().toUriString();
		 }
		 
		 if("login".equalsIgnoreCase(mode)) {
			 ResOAuth2MemberLogin member= null;
			 member = oAuth2Service.findByEmail(principal.getName());
			 
			 if(member == null) {
				 oAuth2Service.createMember(principal);
				 member = oAuth2Service.findByEmail(principal.getName());
			 }
			 request.getSession().invalidate();
			 HttpSession session = request.getSession();
			 session.setAttribute("member_id", member.getMember_id());
			 
			 return UriComponentsBuilder.fromUriString(TokenRedirect.LOGIN_SUCCESS_URL.getUrlText())
					 									   .build().toUriString();
		 } else if("unlink".equalsIgnoreCase(mode)) {
			 
			 String accessToken = principal.userInfo().getAccessToken();
			 OAuth2Provider provider = principal.userInfo().getProdriver();
			 
			 oAuth2UserUnlinkManager.unlink(provider, accessToken);
			 
			 return UriComponentsBuilder.fromUriString(TokenRedirect.LOGOUT_URL.getUrlText())
					 									   .build().toString();
		 }
		 
	        return UriComponentsBuilder.fromUriString(TokenRedirect.LOGIN_FAILED_URL.getUrlText())
	                .queryParam("error", "Login failed")
	                .build().toUriString();
	 }
}
