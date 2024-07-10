package com.planner.oauth.handler;


import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import com.planner.dto.response.member.ResMemberDetail;
import com.planner.oauth.TokenRedirect;
import com.planner.oauth.service.OAuth2Service;
import com.planner.oauth.service.OAuth2UserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final OAuth2Service oAuth2Service;

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		String targetUrl;

		targetUrl = determineTargetUrl(request, response, authentication);

		clearAuthenticationAttributes(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	@Transactional
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {

		OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);
        if (principal == null) {
            log.error("유저 인증 정보가 없습니다.");
            return UriComponentsBuilder.fromUriString(TokenRedirect.LOGIN_FAILED_URL.getUrlText())
                    .queryParam("error", "로그인 인증 정보를 찾을 수 없습니다.")
                    .build().toUriString();
        }
			
			ResMemberDetail member  = oAuth2Service.findByOAuthId(principal.getOAuthId());
			if (member == null) {
				oAuth2Service.createMember(principal);
				member = oAuth2Service.findByOAuthId(principal.getOAuthId());
			}
			
			return UriComponentsBuilder.fromUriString(TokenRedirect.LOGIN_SUCCESS_URL.getUrlText()).build()
					.toUriString();
			
	}
	
	private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		
		if(principal instanceof OAuth2UserPrincipal) {
			return (OAuth2UserPrincipal) principal;
		}
		return null;
	}
	
	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) { }
}
