package com.planner.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.planner.dto.response.member.ResOAuth2MemberLogin;
import com.planner.enums.MemberStatus;
import com.planner.oauth.CookieUtils;
import com.planner.oauth.service.OAuth2Service;
import com.planner.oauth.service.OAuth2UserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PlannerController {

	private final OAuth2Service oAuth2Service;
	
	@GetMapping("/planner/main")
	public String main(@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal,HttpServletRequest request,HttpServletResponse response) {
		if(oAuth2UserPrincipal !=null) {
			ResOAuth2MemberLogin user =  oAuth2Service.findByOAuthId(oAuth2UserPrincipal.getOAuthId());
			if(user.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
				HttpSession session = request.getSession();
				session.invalidate();
				CookieUtils.deleteCookie(request, response, "oauth2_auth_request");
				CookieUtils.deleteCookie(request, response, "mode");
				CookieUtils.deleteCookie(request, response, "redirect_uri");
				return "redirect:/oauth2/signup";
			}
		}
		return"main";
	}
	
}
 