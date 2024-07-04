package com.planner.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.planner.service.TeamMemberService;
import com.planner.service.TeamService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/team/member")
@PreAuthorize("isAuthenticated()")
public class TeamMemberController {

	private final TeamService teamService;
	private final TeamMemberService tmService;

	// 그룹 가입 신청
	@GetMapping("/insert")
	public String tmInsert(Model model, @UserData ResMemberDetail detail,
						@RequestParam(name = "team_id", defaultValue = "-1") Long team_id) {
		// 중복 신청 검사
		String grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
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
		}
		return "/team/member/tminsert";
	}

	// 가입 신청
	@PostMapping("/insert")
	public String tmInsert(@RequestParam(name = "team_id", defaultValue = "-1") Long team_id,
							@UserData ResMemberDetail detail, @RequestParam("nickname") String nickname) {
		tmService.tmInsert(team_id, detail.getMember_id(), nickname);
		return "redirect:/team/member/info?team_id="+team_id+"&member_id="+detail.getMember_id();
	}

	// 가입 수락
	@PatchMapping("/accept")
	@ResponseBody
	public HttpStatus tmaccept(@UserData ResMemberDetail detail, @RequestParam("team_id") Long team_id,
							@RequestParam("member_id") Long member_id) {
		String grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		if(grade != null && grade.indexOf("MASTER") != -1) {
			int result = tmService.accept(team_id, member_id, TM_Grade.ROLE_TEAM_USER.getValue());
			if(result == 1) {
				return HttpStatus.OK;
			}
		}
		return HttpStatus.BAD_REQUEST;
	}

	// tm_grade 변경
	@PatchMapping("/grade-modify")
	@ResponseBody
	public HttpStatus tmGradeModify(@UserData ResMemberDetail detail,
									@RequestParam("team_id") Long team_id,
									@RequestParam("member_id") Long member_id,
									@RequestParam("tm_grade") String tm_grade) {
		if(TM_Grade.grade_set.contains(tm_grade)) {
			int result = tmService.gradeModify(team_id, member_id, tm_grade);
			if(result == 1) {
				if(TM_Grade.ROLE_TEAM_MASTER.getValue().equals(tm_grade)) { // 그룹장 양도시 user로 내려옴
					tmService.gradeModify(team_id, detail.getMember_id(), TM_Grade.ROLE_TEAM_USER.getValue());
				}
				return HttpStatus.OK;
			}
		}
		return HttpStatus.BAD_REQUEST;
	}

	// 그룹 회원 목록
	@GetMapping("/tmlist")
	public String tmInfoList(Model model, @RequestParam(name = "team_id", defaultValue = "-1") Long team_id,
							@UserData ResMemberDetail detail) {
		String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		if(tm_grade != null && !TM_Grade.ROLE_TEAM_WAIT.getValue().equals(tm_grade)) {
			List<TeamMemberDTO> tmlist = tmService.tmInfoList(team_id);
			String wait = TM_Grade.ROLE_TEAM_WAIT.getValue();
			long wait_count = tmlist.stream()
					.filter(dto -> dto.getTm_grade().equals(wait))
					.count();
			model.addAttribute("tmlist", tmlist);
			model.addAttribute("wait_count", wait_count);
			model.addAttribute("tm_grade", tm_grade);
			return "/team/member/tmlist";
		}else {
			return "redirect:/team/main";
		}
	}

	// 그룹 회원 정보
	@GetMapping("/info")
	public String tmInfo(Model model, @UserData ResMemberDetail detail,
						@RequestParam(name = "member_id", defaultValue = "-1") Long member_id,
						@RequestParam(name = "team_member_id", defaultValue = "-1") Long team_member_id,
						@RequestParam(name = "team_id", defaultValue = "-1") Long team_id) {
		TeamMyInfoDTO dto = null;
		if(member_id > 0) {
			dto = tmService.myinfo(team_id, member_id);
		}else if(team_member_id > 0){
			dto = tmService.myinfo2(team_id, team_member_id);
		}else {
			dto = tmService.myinfo(team_id, detail.getMember_id());
		}
		if(dto == null) {
			return "redirect:/team/info?team_id="+team_id;
		}
		model.addAttribute("dto", dto);
		model.addAttribute("member_id", detail.getMember_id());
		return "/team/member/tminfo";
	}

	// 그룹 회원 정보 수정
	@GetMapping("/update")
	public String tmUpdate(Model model, @UserData ResMemberDetail detail,
						@RequestParam("team_id") Long team_id) {
		TeamMyInfoDTO dto = tmService.myinfo(team_id, detail.getMember_id());
		if(dto == null) {
			return "redirect:/team/info?team_id="+team_id;
		}
		model.addAttribute("dto", dto);
		return "/team/member/tmupdate";
	}

	// 그룹 회원 정보 수정
	@PostMapping("/update")
	public String tmUpdate(Model model,@UserData ResMemberDetail detail,
						@RequestParam("team_id") Long team_id,
						@RequestParam("tm_nickname") String tm_nickname){
		tmService.tmUpdate(team_id, detail.getMember_id(), tm_nickname);
		return "redirect:/team/member/info?team_id="+team_id;
	}

	// 그룹 탈퇴
	@DeleteMapping("/delete")
	public ResponseEntity<Void> tmDelete(@RequestParam("team_id") Long team_id,
										@RequestParam("member_id") Long member_id) {
		int result = tmService.tmDelete(team_id, member_id);
		if(result == 1) {
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(URI.create("/planner/main")); // redirect 시킬 경로 설정
			return new ResponseEntity<Void>(headers, HttpStatus.SEE_OTHER);
		}else {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

	// 그룹 추방
	@DeleteMapping("/kick")
	@ResponseBody
	public HttpStatus tmKick(@UserData ResMemberDetail detail, @RequestParam("team_id") Long team_id,
							@RequestParam("member_id") Long member_id) {
		String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		if(TM_Grade.ROLE_TEAM_MASTER.getValue().equals(tm_grade)) {
			int result = tmService.tmDelete(team_id, member_id);
			if(result == 1) {
				return HttpStatus.OK;
			}
		}
		return HttpStatus.BAD_REQUEST;
	}
}
