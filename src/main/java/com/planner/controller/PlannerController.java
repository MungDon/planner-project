package com.planner.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberStatus;
import com.planner.oauth.service.OAuth2Service;
import com.planner.util.UserData;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PlannerController {

	private final OAuth2Service oAuth2Service;
	
	@GetMapping("/planner/main")
	public String main(@UserData ResMemberDetail detail,HttpServletRequest request,HttpServletResponse response) {
		if(detail !=null) {
			if(detail.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
				return "redirect:/oauth2/signup";
			}
		}
		return"main";
	}
	
}
 