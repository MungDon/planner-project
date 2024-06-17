package com.planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.planner.dto.request.member.ReqOAuth2Signup;
import com.planner.oauth.service.OAuth2Service;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class OAuth2Controller {

	private final OAuth2Service oAuth2Service;
	
	/*소셜로그인에서 못받은 정보 받기 FORM*/
	@GetMapping("/signup")
	public String oAuth2SignupForm() {
		return "oauth2Signup";
	}
	
	/*소셜로그인에서 못받은 정보 저장*/	
	@PostMapping("/signup")
	public String oAuth2Signup(ReqOAuth2Signup req) {
		oAuth2Service.fetchAdditionalUserInfo(req);
		return "redirect://planner/main";
		//TODO - 메인 링크 추후 바뀜
	}
}