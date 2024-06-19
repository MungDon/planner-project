package com.planner.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.request.member.ReqMemberUpdate;
import com.planner.dto.request.member.ReqPasswordChk;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.Gender;
import com.planner.oauth.CookieUtils;
import com.planner.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member/*")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	
	/*소셜로그인에서 생긴 쿠키제거 후 로그아웃*/
	@GetMapping("signout")
	public String signout(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.deleteCookie(request, response, "oauth2_auth_request");
		CookieUtils.deleteCookie(request, response, "mode");
		CookieUtils.deleteCookie(request, response, "redirect_uri");
		return "redirect:/member/logout";
	}


	//	회원가입 Get
	@GetMapping("insert")
	public String memberInsert() {
		return "/member/member_insert";
	}
	
	//	회원가입 Post
	@PostMapping("insert")
	public String memberInsert(MemberDTO memberDTO, RedirectAttributes rttr) {
		int result = memberService.memberInsert(memberDTO);
		rttr.addFlashAttribute("result", result);
		return "redirect:/planner/main";
	}

	//	로그인
	@GetMapping("login")
	public String memberLogin() {
		return "/member/member_login";
	}
	
	/*내 정보*/
	@PreAuthorize("isAuthenticated()")
	@GetMapping("info")
	public String memberInfo(Model model,Principal principal) {
		ResMemberDetail detail = memberService.memberDetail(principal.getName());
		String gender = Gender.findNameByCode(detail.getMember_gender());
		model.addAttribute("detail", detail);
		model.addAttribute("gender", gender);
		return "/member/member_info"; 
	}
	
	/*비밀번호 확인 폼*/
	@PreAuthorize("isAuthenticated()")
	@GetMapping("chk")
	public String passwordChkForm(@RequestParam(value = "url")String url,Model model) {
		model.addAttribute("url", url);
		return"/member/passwordChk";
	}
	
	/*비밀번호 확인*/
	@PreAuthorize("isAuthenticated()")
	@PostMapping("chk")
	public String passwordChk(ReqPasswordChk req) {
		return String.format("redirect:/member/%s", req.getUrl());
	}
	/*회원정보 수정 폼*/
	@PreAuthorize("isAuthenticated()")
	@GetMapping("update")
	public String memberUpdateForm(Principal principal,Model model) {
		ResMemberDetail detail = memberService.memberDetail(principal.getName());
		model.addAttribute("detail", detail);
		return "/member/member_update";
	}

	/*회원정보 수정*/
	@PreAuthorize("isAuthenticated()")
	@PutMapping("update")
	public String memberUpdate(ReqMemberUpdate req) {
		memberService.memberUpdate(req);
		//TODO - 회원 정보 이메일 수정시에 이메일 인증 추가
		return "redirect:/member/info";
	}
	
	/*회원 탈퇴*/
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("delete")
	public void memberDelete() {
		
	}
}