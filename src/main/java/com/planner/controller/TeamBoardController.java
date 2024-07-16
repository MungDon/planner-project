package com.planner.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.request.schedule.ScheduleDTO;
import com.planner.dto.request.team.TeamDTO;
import com.planner.dto.request.team.board.TeamBoardDTO;
import com.planner.dto.request.team.board.TeamBoardListDTO;
import com.planner.dto.request.team.board.TeamBoardUpdateDTO;
import com.planner.dto.request.team.vote.VoteDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.TM_Grade;
import com.planner.service.ScheduleService;
import com.planner.service.TeamBoardService;
import com.planner.service.TeamMemberService;
import com.planner.service.TeamService;
import com.planner.service.VoteService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/team/board")
@PreAuthorize("isAuthenticated()")
public class TeamBoardController {

	private final TeamService teamService;
	private final TeamMemberService tmService;
	private final TeamBoardService tbService;
	private final VoteService voteService;
	private final ScheduleService scheduleService;

	// 게시글 목록
	@GetMapping("/list")
	public String tblist(Model model, @UserData ResMemberDetail detail,
			@RequestParam(name = "team_id", defaultValue = "-1") Long team_id,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "ca", defaultValue = "전체") String category,
			@RequestParam(name = "ps", defaultValue = "15") int pageSize,
			@RequestParam(name = "so", defaultValue = "NO") String searchOption,
			@RequestParam(name = "search", defaultValue = "") String search) {
		String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		if(tm_grade == null || TM_Grade.ROLE_WAIT.getValue().equals(tm_grade)) {
			// 그룹 가입한 사람 아니면 redirect
			return "redirect:/team/main";
		}
		// 검색어 유효성 검사
		if(search.length() != 0 && search.strip().length() < 2) {
			return "redirect:/team/board/list?team_id="+team_id;
		}
		TeamDTO teamDTO = teamService.teamInfo(team_id);
		int tbCount = tbService.tbCount(team_id, category, searchOption, search);
		List<TeamBoardListDTO> list = null;
		// 검색된 게시글이 있으면
		if (tbCount > 0) {
			list = tbService.tblist(team_id, category, searchOption, search, pageNum, pageSize);
			// 페이지 번호 정상적인지 검사
			if(list.size() == 0) {
				return "redirect:/team/board/list?team_id="+team_id;
			}
		}
		List<TeamBoardListDTO> notice_list = null;
		if (!category.equals("공지사항") && search.length() == 0) {
			// 최신 공지사항 10개까지 상단에 따로 표시
			notice_list = tbService.tblist(team_id, "공지사항", "NO", "", 1, 10);
		}
		int pageBlock = 10;
		int startPage = ((pageNum - 1) / pageBlock) * pageBlock + 1;
		int endPage = startPage + pageBlock - 1;
		int totalPage = tbCount / pageSize + (tbCount % pageSize == 0 ? 0 : 1);
		if (endPage > totalPage) {
			endPage = totalPage;
		}

		model.addAttribute("team_id", team_id);
		model.addAttribute("teamDTO", teamDTO);
		model.addAttribute("list", list);
		model.addAttribute("notice_list", notice_list);
		model.addAttribute("tbCount", tbCount);
		model.addAttribute("ca", category);
		model.addAttribute("pageBlock", pageBlock);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("ps", pageSize);
		model.addAttribute("so", searchOption);
		model.addAttribute("search", search);
		return "/team/board/tblist";
	}

	// 게시글 작성
	@GetMapping("/write")
	public String tbwrite(Model model, @ModelAttribute("team_id") Long team_id,
						@UserData ResMemberDetail detail) {
		String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
		if(tm_grade == null || TM_Grade.ROLE_WAIT.getValue().equals(tm_grade)) {
			return "redirect:/team/main";
		}
		model.addAttribute("tm_grade", tm_grade);
		return "/team/board/tbwrite";
	}

	// 게시글 작성
	@PostMapping("/write")
	@Transactional
	public String tbwrite(TeamBoardDTO dto, VoteDTO vdto, @UserData ResMemberDetail detail,
			@RequestParam(name = "vote_item_name", defaultValue = "") List<String> vote_item_names) {
		tbService.tbInsert(dto, detail.getMember_id());
		if(vdto != null) {
			vdto.setTeam_board_id(dto.getTeam_board_id());
			voteService.voteInsert(vdto);
			voteService.voteItemInsert(vdto, vote_item_names);
			dto.setVote_id(vdto.getVote_id());
		}
		return "redirect:/team/board/view?team_id=" + dto.getTeam_id() + "&tb_id=" + dto.getTeam_board_id();
	}

	// 게시글 확인
	@GetMapping("/view")
	public String tbview(Model model, @UserData ResMemberDetail detail, 
			@RequestParam("tb_id") Long team_board_id,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "ca", defaultValue = "전체") String category,
			@RequestParam(name = "ps", defaultValue = "15") int pageSize,
			@RequestParam(name = "so", defaultValue = "NO") String searchOption,
			@RequestParam(name = "search", defaultValue = "") String search) {
		TeamBoardDTO dto = tbService.teamBoardView(team_board_id);
		if(dto == null) {
			return "redirect:/team/main";
		}
		String tm_grade = tmService.teamMemberGrade(dto.getTeam_id(), detail.getMember_id());
		if(tm_grade == null || TM_Grade.ROLE_WAIT.getValue().equals(tm_grade)) {
			return "redirect:/team/main";
		}
		long team_member_id = tmService.teamMemberId(dto.getTeam_id(), detail.getMember_id());
		if(dto.getSchedule_id() != null) {
			// 게시글에 일정이 있다면 추가. 투표는 있으면 스크립트로 요청함.
			ScheduleDTO scheduleDTO = scheduleService.schedule_select_one(dto.getSchedule_id());
			model.addAttribute("scheduleDTO", scheduleDTO);
		}
		model.addAttribute("dto", dto);
		model.addAttribute("tm_grade", tm_grade);
		model.addAttribute("team_member_id", team_member_id);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("ca", category);
		model.addAttribute("ps", pageSize);
		model.addAttribute("so", searchOption);
		model.addAttribute("search", search);
		return "/team/board/tbview";
	}

	// 게시글 수정
	@GetMapping("/modify")
	public String tbmodify(Model model, @RequestParam("tb_id") Long team_board_id,
			@ModelAttribute("team_id") Long team_id, @UserData ResMemberDetail detail) {
		TeamBoardDTO dto = tbService.teamBoardView(team_board_id);
		long team_member_id = tmService.teamMemberId(dto.getTeam_id(), detail.getMember_id());
		if(dto.getTeam_member_id() != team_member_id) {
			// 작성자 아니면 게시글 수정 못하게 접근 차단
			return "redirect:/team/board/list?team_id="+team_id;
		}
		// grade 보내는 이유는 공지사항 때문임.
		String tm_grade = tmService.teamMemberGrade(dto.getTeam_id(), detail.getMember_id());
		if(dto.getTb_category().equals("공지사항") && tm_grade.equals(TM_Grade.ROLE_TEAM_USER.getValue())) {
			dto.setTb_category("일반");
		}
		model.addAttribute("dto", dto);
		model.addAttribute("tm_grade", tm_grade);
		return "/team/board/tbmodify";
	}

	// 게시글 수정
	@PostMapping("/modify")
	public String tbmodify(TeamBoardUpdateDTO dto, @UserData ResMemberDetail detail,
			@RequestParam("team_id")long team_id) {
		long team_member_id = tmService.teamMemberId(team_id, detail.getMember_id());
		dto.setTeam_member_id(team_member_id);
		tbService.teamBoardUpdate(dto);
		return "redirect:/team/board/view?tb_id=" + dto.getTeam_board_id();
	}

	// 게시글 삭제
	@ResponseBody
	@PostMapping("/delete")
	public String tbdelete(@RequestParam("team_id") Long team_id, @RequestParam("tb_id") Long team_board_id) {
		tbService.teamBoardDelete(team_board_id);
		return "/team/board/list?team_id=" + team_id;
	}
}
