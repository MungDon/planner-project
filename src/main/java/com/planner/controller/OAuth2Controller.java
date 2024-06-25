package com.planner.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.planner.dto.request.member.ReqOAuth2Signup;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.oauth.service.OAuth2Service;
import com.planner.oauth.service.OAuth2UserPrincipal;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class OAuth2Controller {
	
	private final OAuth2Service oAuth2Service;
	
	/*소셜로그인에서 못받은 정보 받기 FORM*/
	@GetMapping("/auth/signup")
	public String oAuth2SignupForm(@UserData ResMemberDetail member, Model model) {
		model.addAttribute("member", member);
		return "/member/oauth2Signup";
	}
	
	/*소셜로그인에서 못받은 정보 저장*/	
	@PostMapping("/auth/signup")
	public String oAuth2Signup(@AuthenticationPrincipal OAuth2UserPrincipal principal,ReqOAuth2Signup req) {
		if(principal != null) {
			oAuth2Service.fetchAdditionalUserInfo(req, principal);
		}else {
			return "redirect:/oauth2/auth/signup";
		}
		return "redirect:/planner/main";
		//TODO - 메인 링크 추후 바뀜
	}
}
