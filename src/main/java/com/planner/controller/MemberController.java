package com.planner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.planner.oauth.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member/*")
@RequiredArgsConstructor
public class MemberController {

	
	@GetMapping("/signout")
	public String signout(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.deleteCookie(request, response, "oauth2_auth_request");
		CookieUtils.deleteCookie(request, response, "mode");
		CookieUtils.deleteCookie(request, response, "redirect_uri");
		return "redirect:/member/logout";
	}
}
