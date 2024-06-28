package com.planner.controller;

import java.security.Principal;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.planner.dto.request.team.TeamDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.service.TeamService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class TeamController {
	
	// TODO 합치고 나면 로그인 회원의 권한과 관련된 부분들 수정해야됨
	// team 정보 수정, 삭제 같은것들.
	// 주소창에 직접 적어서 임의로 접근하는 것들 대비해서 기본값 설정, 예외처리 필요함. 특히 team_id 관련된 부분
	
	private final TeamService teamService;
	
	// 편하게 실행하기 위한 임시 메인
	@GetMapping("/")
	public String tempMain() {
		return "redirect:/team/main";
	}
	
	@GetMapping("/team/main")
	public String teamMain() {
		return "/team/teamMain";
	}
	
	@GetMapping("/team/create")
	public String teamCreate(@ModelAttribute("member_id") Long member_id) {
		return "/team/teamCreate";
	}
	
	@PostMapping("/team/create")
	public String teamCreate(Model model, @UserData ResMemberDetail detail,
							@ModelAttribute("team_name") String team_name, @ModelAttribute("team_explain") String team_explain,
							@RequestParam("team_image") MultipartFile team_image) {
		boolean result = teamService.teamNameOverlap(team_name); // 중복 검사. 중복이면 false
		if(!result) { // 중복이면
			model.addAttribute("result", "중복된 그룹 이름입니다.");
			return "/team/teamCreate";
		}
		boolean result2 = teamService.teamCreate(detail, team_name, team_explain, team_image); // 등록 실패시 false
		if(!result2) {
			model.addAttribute("result", "지원하지 않는 형식의 이미지입니다.");
			return "/team/teamCreate";
		}
		
		return "redirect:/team/main";
	}
	
	@GetMapping("/team/info")
	public String teamInfo(Model model, @RequestParam(name="team_id", defaultValue = "-1") long team_id) {
		TeamDTO dto = teamService.teamInfo(team_id);
		model.addAttribute("dto", dto);
		if(dto == null) {
			model.addAttribute("nodto", "잘못된 접근입니다.");
		}
		return "/team/teamInfo";
	}
	
	@GetMapping("/team/update")
	public String teamUpdate(Model model, Principal principal, @RequestParam("team_id") long team_id) {
		// 로그인 회원 정보, 권한 확인해야함.
		TeamDTO dto = teamService.teamInfo(team_id);
		model.addAttribute("dto", dto);
		return "/team/teamUpdate";
	}
	
	@PostMapping("/team/update")
	public String teamUpdate(Principal principal, Model model, @RequestParam("team_id") long team_id,
							@ModelAttribute("team_name") String team_name, @ModelAttribute("team_explain") String team_explain,
							@RequestParam("team_image") MultipartFile team_image, @RequestParam(name = "delimg", defaultValue = "") String delimg) {
		teamService.teamInfoUpdate(principal, team_id, team_name, team_explain, team_image, delimg);
		return "redirect:/team/info?team_id="+team_id;
	}
	
	@GetMapping("/team/delete")
	public String teamDelete(@RequestParam("team_id") long team_id, @RequestParam("member_id") long member_id) {
		teamService.teamDelete(team_id, member_id);
		return "redirect:/";
	}
	
	@GetMapping("/team/img")
	public ResponseEntity<Resource> displayImg(@RequestParam("img") String imgName){
		return teamService.teamImg(imgName);
	}
}
