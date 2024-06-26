package com.planner.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberStatus;
import com.planner.util.UserData;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PlannerController {

	
	@GetMapping("/planner/main")
	public String main(@UserData ResMemberDetail detail,HttpServletRequest request,HttpServletResponse response,Model model) {
		if(detail !=null) {
			if(detail.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
				return "redirect:/oauth2/auth/signup";
			}
		model.addAttribute("member", detail);
		}
		return"main";
	}
	
}
 