package com.planner.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.planner.dto.request.team.MyTeamListDTO;
import com.planner.dto.request.team.TeamDTO;
import com.planner.dto.request.team.TeamInfoDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.TM_Grade;
import com.planner.service.TeamMemberService;
import com.planner.service.TeamService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@PreAuthorize("isAuthenticated()")
public class TeamController {

	private final TeamService teamService;
	private final TeamMemberService tmService;

	// 팀 메인
	@GetMapping("/team/main")
	public String teamMain(@UserData ResMemberDetail detail, Model model) {
		List<MyTeamListDTO> teamList = teamService.myTeamList(detail.getMember_id());
		model.addAttribute("teamList", teamList);
		return "/team/teamMain";
	}

	// 그룹 이름 중복 검사
	@GetMapping("/team/overlap")
	@ResponseBody
	public int teamNameOverlap(@RequestParam("team_name") String team_name) {
		boolean result = teamService.teamNameOverlap(team_name);
		if(result) { // 중복임
			return -1;
		}else { // 중복아님
			return 1;
		}
	}

	// 그룹 생성
	@GetMapping("/team/create")
	public String teamCreate() {
		return "/team/teamCreate";
	}

	// 그룹 생성
	@PostMapping("/team/create")
	public String teamCreate(Model model, @UserData ResMemberDetail detail,
							@ModelAttribute("team_name") String team_name, @ModelAttribute("team_explain") String team_explain,
							@RequestParam("team_image") MultipartFile team_image) {
		boolean result = teamService.teamNameOverlap(team_name);
		if(result) { // 중복이면
			model.addAttribute("msg", "중복된 그룹 이름입니다.");
			return "/team/teamCreate";
		}
		long team_id = teamService.teamCreate(detail, team_name, team_explain, team_image);
		if(team_id == -1) { // 이미지 저장 실패시 -1
			model.addAttribute("msg", "지원하지 않는 형식의 이미지입니다.");
			return "/team/teamCreate";
		}

		return "redirect:/team/info?team_id="+team_id;
	}

	// 그룹 정보
	@GetMapping("/team/info")
	public String teamInfo(Model model, @UserData ResMemberDetail detail,
						@RequestParam(name="team_id", defaultValue = "-1") long team_id) {
		TeamInfoDTO dto = teamService.teamAndMasterInfo(team_id);
		String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		model.addAttribute("dto", dto);
		model.addAttribute("tm_grade", tm_grade);
		if(dto == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
		}
		return "/team/teamInfo";
	}

	// 그룹 정보 수정
	@GetMapping("/team/update")
	public String teamUpdate(Model model, @UserData ResMemberDetail detail, @RequestParam("team_id") long team_id) {
		String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		if(TM_Grade.ROLE_TEAM_MASTER.getValue().equals(tm_grade)) {
			TeamDTO dto = teamService.teamInfo(team_id);
			model.addAttribute("dto", dto);
			return "/team/teamUpdate";
		}else {
			return "redirect:/team/main";
		}
	}

	// 그룹 정보 수정
	@PostMapping("/team/update")
	public String teamUpdate(@UserData ResMemberDetail detail, Model model, @RequestParam("team_id") long team_id,
							@ModelAttribute("team_name") String team_name, @ModelAttribute("team_explain") String team_explain,
							@RequestParam("team_image") MultipartFile team_image, @RequestParam(name = "delimg", defaultValue = "") String delimg) {
		String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		if(TM_Grade.ROLE_TEAM_MASTER.getValue().equals(tm_grade)) {
			boolean result = teamService.teamNameOverlap(team_name);
			if(result) { // 해당 team_name으로 검색된게 있음
				TeamDTO dto = teamService.teamInfo(team_id);
				if(!dto.getTeam_name().equals(team_name)) { // team_name 변경하려던게 중복이면.
					model.addAttribute("dto", dto);
					model.addAttribute("msg", "중복된 그룹 이름입니다.");
					return "/team/teamUpdate";
				}
			}
			teamService.teamInfoUpdate(team_id, team_name, team_explain, team_image, delimg);
			
		}
		return "redirect:/team/info?team_id="+team_id;
	}

	// 그룹 삭제
	@DeleteMapping("/team/delete")
	public String teamDelete(@RequestParam("team_id") long team_id, @UserData ResMemberDetail detail) {
		String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		if(TM_Grade.ROLE_TEAM_MASTER.getValue().equals(tm_grade)) {
			teamService.teamDelete(detail.getMember_id(), team_id);
		}
		return "redirect:/team/main";
	}

	// 그룹 찾기
	@GetMapping("/team/search")
	public String teamSearch(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "so", defaultValue = "NO") String searchOption,
			@RequestParam(name = "search", defaultValue = "") String search) {
		int pageSize = 20; // 한 페이지에 표시할 개수
		int pageBlock = 10; // 한번에 표시할 페이지 번호 개수
		// get 방식이라 스크립트 무시하고 주소창에서 검색어 쳤을때 대비 유효성 검사
		if(search.length() != 0 && search.strip().length() < 2) {
			return "redirect:/team/search";
		}
		long count = teamService.teamCount(searchOption, search); // 팀 개수
		long pageCount = count/pageSize + count%pageSize == 0 ? 0 : 1; // 페이지 개수
		long pageStart = ((page-1)/pageBlock)*pageBlock + 1; // 출력할 첫 페이지 번호
		long pageEnd = pageStart + pageBlock - 1; // 출력할 마지막 페이지 번호
		if(pageEnd > pageCount) {
			pageEnd = pageCount;
		}
		List<TeamInfoDTO> list = teamService.teamListSearch(pageSize, page, searchOption, search); // 팀 정보 list
		// page 번호 주소창에서 임의로 변경했을 때, 있는 페이지인지 검사
		if(list.size()==0 && count > 0) {
			return "redirect:/team/search";
		}
		model.addAttribute("count", count);
		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageBlock", pageBlock);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("pageStart", pageStart);
		model.addAttribute("pageEnd", pageEnd);
		model.addAttribute("so", searchOption);
		model.addAttribute("search", search);
		return "/team/teamSearch";
	}

	// 그룹 이미지 출력
	@GetMapping("/team/img")
	public ResponseEntity<Resource> displayImg(@RequestParam("img") String imgName){
		return teamService.teamImg(imgName);
	}
}
