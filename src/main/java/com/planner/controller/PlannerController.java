package com.planner.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.planner.dto.response.member.ResOAuth2MemberLogin;
import com.planner.enums.MemberStatus;
import com.planner.oauth.service.OAuth2Service;
import com.planner.oauth.service.OAuth2UserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PlannerController {

	private final OAuth2Service oAuth2Service;
	
	@GetMapping("/planner/main")
	public String main(@AuthenticationPrincipal OAuth2UserPrincipal principal , HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		if(session != null && principal !=null) {
			// 로그인한 유저 정보가져오기
			ResOAuth2MemberLogin user = oAuth2Service.findByOAuthId(principal.getOAuthId());
			System.out.println("???");
			// 가져온 유저정보의 유저 상태코드가 NOT_DONE("N") 가입 미완이면 가입페이지로 이동
			if(user.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
				return "redirect://oauth2/signup";
			}
		}
		// 아니면 메인으로
		return"main";
	}
	
	@GetMapping("/test")
	public String test(Model model) {
		ResOAuth2MemberLogin user = oAuth2Service.findByOAuthId("Z");
		System.out.println(user.getMember_name());
		model.addAttribute("user", user);
		return "test";
	}
	
}
