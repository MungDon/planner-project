package com.planner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.request.member.ReqMemberRestore;
import com.planner.dto.request.member.ReqMemberUpdate;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.Gender;
import com.planner.enums.MemberStatus;
import com.planner.exception.CustomException;
import com.planner.exception.ErrorCode;
import com.planner.service.MemberService;
import com.planner.util.CommonUtils;
import com.planner.util.UserData;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	
	/*소셜로그인에서 생긴 쿠키 제거 후 로그아웃*/
	@GetMapping("/anon/signout")
	public String signout(HttpServletRequest request, HttpServletResponse response) {
		CommonUtils.removeCookiesAndSession(request, response);
		return "redirect:/member/logout";
	}


	//	회원가입 Get
	@GetMapping("/anon/insert")
	public String memberInsert() {
		return "/member/member_insert";
	}
	
	//	회원가입 Post
	@PostMapping("/anon/insert")
	public String memberInsert(MemberDTO memberDTO, RedirectAttributes rttr) {
		int result = memberService.memberInsert(memberDTO);
		rttr.addFlashAttribute("result", result);
		return "redirect:/planner/main";
	}

	//	로그인
	@GetMapping("/anon/login")
	public String memberLogin(@UserData ResMemberDetail detail,HttpServletRequest request,HttpServletResponse response) {
			if(detail != null &&detail.getMember_status().equals(MemberStatus.NOT_DONE.getCode())) {
				CommonUtils.removeCookiesAndSession(request, response);
				return"redirect:/member/anon/login";
			}
		return "/member/member_login";
	}

	/*로그인시에 회원탈퇴여부 검사*/
	@GetMapping("/anon")
	public String memberChk(@UserData ResMemberDetail detail, HttpServletRequest request,HttpServletResponse response) {
		if(detail.getMember_status().equals(MemberStatus.DELETE.getCode())) {
			CommonUtils.removeCookiesAndSession(request, response);
			throw new CustomException(ErrorCode.WITHDRAWN_MEMBER);
		}
		return "redirect:/planner/main";
	}
	
	/*로그인 실패시 매핑*/
	@GetMapping("/anon/fail")
	public void loginFail() {
		throw new CustomException(ErrorCode.NO_ACCOUNT);
	}
	
	/*내 정보*/
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/auth/info")
	public String memberInfo(Model model,@UserData ResMemberDetail detail) {
		String gender = Gender.findNameByCode(detail.getMember_gender());
		model.addAttribute("detail", detail);
		model.addAttribute("gender", gender);
		return "/member/member_info"; 
	}
	
	/*비밀번호 확인 폼*/
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/auth/chk")
	public String passwordChkForm(@RequestParam(value = "url")String url, Model model) {
		model.addAttribute("url", url);
		return"/member/passwordChk";
	}
	
	/*비밀번호 확인*/
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/auth/chk")
	@ResponseBody
	public ResponseEntity<String> passwordChk(@RequestParam(value = "currentPw")String currentPw,@UserData ResMemberDetail member) {
		int result = memberService.passwordChk(currentPw,member);
		if(result ==1) {
			return ResponseEntity.ok("성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("실패");
	}
	
	/*회원정보 수정 폼*/
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/auth/update")
	public String memberUpdateForm(Model model,@UserData ResMemberDetail detail) {
		model.addAttribute("detail", detail);
		return "/member/member_update";
	}

	/*회원정보 수정*/
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/auth/update")
	public String memberUpdate(ReqMemberUpdate req) {
		memberService.memberUpdate(req);
		//TODO - 회원 정보 이메일 수정시에 이메일 인증 추가
		return "redirect:/member/auth/info";
	}
	
	/*회원 탈퇴*/
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/auth/delete")
	@ResponseBody
	public void memberDelete(@UserData ResMemberDetail detail,HttpServletRequest request,HttpServletResponse response) {
		CommonUtils.removeCookiesAndSession(request, response);
		memberService.memberDelete(detail.getMember_id());
	}
	
	/*회원복구 폼*/
	@GetMapping("/anon/restore")
	public String memberRestoreForm() {
		return "/member/member_restore";
	}
	
	/*회원 복구*/
	@PostMapping("/anon/restore")
	@ResponseBody
	public int memberRestore(ReqMemberRestore req) {
		int result = memberService.memberRestore(req);
		return result;
	}
}