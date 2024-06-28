package com.planner.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.request.team.TeamDTO;
import com.planner.dto.request.team.TeamMemberDTO;
import com.planner.dto.request.team.TeamMyInfoDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.TM_Grade;
import com.planner.service.MemberService;
import com.planner.service.TeamMemberService;
import com.planner.service.TeamService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/team/member")
public class TeamMemberController {

	private final TeamService teamService;
	private final TeamMemberService tmService;
	
	@GetMapping("/insert")
	public String tmInsert(Model model, @RequestParam(name = "team_id", defaultValue = "-1") Long team_id, @UserData ResMemberDetail detail) {
		// 중복 신청 검사
		String grade = tmService.teamMemberOverlap(team_id, detail.getMember_id());
		if(grade != null && TM_Grade.grade_set.contains(grade)) {
			if(TM_Grade.ROLE_TEAM_WAIT.getValue().equals(grade)) {
				model.addAttribute("overlap", "이미 가입 신청중인 그룹입니다.");
			}else {
				model.addAttribute("overlap", "이미 가입된 그룹입니다.");
			}
		}else {
			TeamDTO dto = teamService.teamInfo(team_id);
			model.addAttribute("dto", dto);
			model.addAttribute("member_name", detail.getMember_name());
			model.addAttribute("member_id", detail.getMember_id());
		}
		return "/team/member/tminsert";
	}
	
	@PostMapping("/insert")
	public String tmInsert(@RequestParam(name = "team_id", defaultValue = "-1") Long team_id,
							@RequestParam("member_id") Long member_id, @RequestParam("nickname") String nickname) {
		tmService.tmInsert(team_id, member_id, nickname);
		return "redirect:/team/member/info?team_id="+team_id+"&member_id="+member_id;
	}
	
	@PatchMapping("/accept")
	@ResponseBody
	public HttpStatus tmaccept(@RequestParam("team_id") Long team_id,
							@RequestParam("member_id") Long member_id) {
		int result = tmService.accept(team_id, member_id, TM_Grade.ROLE_TEAM_USER.getValue());
		if(result == 1) {
			return HttpStatus.OK;
		}else {
			return HttpStatus.BAD_REQUEST;
		}
	}
	
	// TODO tm_grade 변경인데 아직 사용 안했음.
	@PatchMapping("/grade-modify")
	@ResponseBody
	public HttpStatus tmGradeModify(@RequestParam("team_id") Long team_id,
			@RequestParam("member_id") Long member_id,
			@RequestParam("tm_grade") String tm_grade) {
		if(TM_Grade.grade_set.contains(tm_grade)) {
			int result = tmService.gradeModify(team_id, member_id, tm_grade);
			if(result == 1) {
				return HttpStatus.OK;
			}
		}
		return HttpStatus.BAD_REQUEST;
	}
	
	@GetMapping("/tmlist")
	public String tmInfoList(Model model, @RequestParam(name = "team_id", defaultValue = "-1") Long team_id) {
		List<TeamMemberDTO> tmlist = tmService.tmInfoList(team_id);
		String wait = TM_Grade.ROLE_TEAM_WAIT.getValue();
		long wait_count = tmlist.stream()
				.filter(dto -> dto.getTm_grade().equals(wait))
				.count();
		model.addAttribute("tmlist", tmlist);
		model.addAttribute("wait_count", wait_count);
		return "/team/member/tmlist";
	}
	
	@GetMapping("/info")
	public String tmInfo(Model model, @RequestParam("member_id") Long member_id, 
						@RequestParam(name = "team_id", defaultValue = "-1") Long team_id) {
		TeamMyInfoDTO dto = tmService.myinfo(team_id, member_id);
		model.addAttribute("dto", dto);
		return "/team/member/tminfo";
	}

	@GetMapping("/update")
	public String tmUpdate(Model model, @RequestParam("member_id") Long member_id,
						@RequestParam("team_id") Long team_id) {
		TeamMyInfoDTO dto = tmService.myinfo(team_id, member_id);
		model.addAttribute("dto", dto);
		return "/team/member/tmupdate";
	}
	
	@PostMapping("/update")
	public String tmUpdate(Model model, @RequestParam("member_id") Long member_id,
						@RequestParam("team_id") Long team_id,
						@RequestParam("tm_nickname") String tm_nickname){
		tmService.tmUpdate(team_id, member_id, tm_nickname);
		return "redirect:/team/member/info?team_id="+team_id+"&member_id="+member_id;
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> tmDelete(@RequestParam("team_id") Long team_id,
										@RequestParam("member_id") Long member_id) {
		int result = tmService.tmDelete(team_id, member_id);
		if(result == 1) {
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(URI.create("/")); // redirect 시킬 경로 설정
			return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
		}else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/kick")
	@ResponseBody
	public HttpStatus tmKick(@RequestParam("team_id") Long team_id,
							@RequestParam("member_id") Long member_id) {
		int result = tmService.tmDelete(team_id, member_id);
		if(result == 1) {
			return HttpStatus.OK;
		}else {
			return HttpStatus.BAD_REQUEST;
		}
	}
}
