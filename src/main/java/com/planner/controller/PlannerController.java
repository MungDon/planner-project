package com.planner.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.planner.dto.response.member.ResOAuth2MemberLogin;
import com.planner.enums.MemberStatus;
import com.planner.oauth.service.OAuth2Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PlannerController {

	private final OAuth2Service oAuth2Service;
	
	@GetMapping("/planner/main")
	public String main(Authentication authentication ,@AuthenticationPrincipal OAuth2User user, HttpServletRequest request) {
		HttpSession session = request.getSession();
	//	OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//		if(session != null || user !=null) {
//			// 로그인한 유저 정보가져오기
//			ResOAuth2MemberLogin user2 = oAuth2Service.findByOAuthId(user.getName());
//			System.out.println(user2.getMember_status());
//			// 가져온 유저정보의 유저 상태코드가 NOT_DONE("N") 가입 미완이면 가입페이지로 이동
//			if(user2.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
//				return "redirect://oauth2/signup";
//			}
//		}
		// 아니면 메인으로
		return"main";
	}
	
}
