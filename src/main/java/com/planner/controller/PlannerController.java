package com.planner.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberStatus;
import com.planner.oauth.service.OAuth2Service;
import com.planner.oauth.service.OAuth2UserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PlannerController {

	private final OAuth2Service oAuth2Service;
	
	@GetMapping("/planner/main")
	public String main(@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal,HttpServletRequest request,HttpServletResponse response) {
		if(oAuth2UserPrincipal !=null) {
			System.out.println("동작함?");
			ResMemberDetail user =  oAuth2Service.findByOAuthId(oAuth2UserPrincipal.getOAuthId());
			if(user.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
				return "redirect:/oauth2/signup";
			}
		}
		return"main";
	}
	
}
 