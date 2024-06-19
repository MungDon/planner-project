package com.planner.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.planner.dto.response.member.ResOAuth2MemberLogin;
import com.planner.enums.MemberStatus;
import com.planner.oauth.service.OAuth2Service;
import com.planner.oauth.service.OAuth2UserPrincipal;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PlannerController {

	private final OAuth2Service oAuth2Service;
	
	@GetMapping("/planner/main")
	public String main(@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal) {
		if(oAuth2UserPrincipal !=null) {
			ResOAuth2MemberLogin user =  oAuth2Service.findByOAuthId(oAuth2UserPrincipal.getOAuthId());
			if(user.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
				return "redirect:/oauth2/signup";
			}
		}
		return"main";
	}
	
}
 