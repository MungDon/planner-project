package com.planner.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.request.team.TeamDTO;
import com.planner.dto.request.team.board.TeamBoardDTO;
import com.planner.dto.request.team.board.TeamBoardListDTO;
import com.planner.dto.request.team.vote.VoteDTO;
import com.planner.service.TeamBoardService;
import com.planner.service.TeamService;
import com.planner.service.VoteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/team/board")
public class TeamBoardController {

	private final TeamService teamService;
	private final TeamBoardService teamBoardService;
	private final VoteService voteService;

	@GetMapping("/list")
	public String tblist(Model model, @RequestParam(name = "team_id", defaultValue = "-1") Long team_id,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "ca", defaultValue = "전체") String category,
			@RequestParam(name = "ps", defaultValue = "15") int pageSize,
			@RequestParam(name = "so", defaultValue = "NO") String searchOption,
			@RequestParam(name = "search", defaultValue = "") String search) {
		TeamDTO teamDTO = teamService.teamInfo(team_id);
		if (teamDTO == null) {
			return "redirect:/";
		}
		int tbCountAll = teamBoardService.tbCountAll(team_id);
		int tbCount = teamBoardService.tbCount(team_id, category, searchOption, search);
		List<TeamBoardListDTO> list = null;
		if (tbCount != 0) {
			list = teamBoardService.tblist(team_id, category, searchOption, search, pageNum, pageSize);
		}
		List<TeamBoardListDTO> notice_list = null;
		if (!category.equals("공지사항")) {
			notice_list = teamBoardService.tblist(team_id, "공지사항", "NO", "", 1, 10);
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
		model.addAttribute("tbCountAll", tbCountAll);
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

	@GetMapping("/write")
	public String tbwrite(@ModelAttribute("team_id") Long team_id,
			@ModelAttribute("team_member_id") Long team_member_id) {

		return "/team/board/tbwrite";
	}

	@PostMapping("/write")
	public String tbwrite(TeamBoardDTO dto, VoteDTO vdto,
			@RequestParam(name = "vote_item_name", defaultValue = "") List<String> vote_item_names) {
		voteService.voteInsert(vdto);
		voteService.voteItemInsert(vdto, vote_item_names);
		dto.setVote_id(vdto.getVote_id());
		teamBoardService.tbInsert(dto);
		return "redirect:/team/board/view?team_id=" + dto.getTeam_id() + "&tb_id=" + dto.getTeam_board_id();
	}

	@GetMapping("/view")
	public String tbview(Model model, @RequestParam("tb_id") Long team_board_id,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "ca", defaultValue = "전체") String category,
			@RequestParam(name = "ps", defaultValue = "15") int pageSize,
			@RequestParam(name = "so", defaultValue = "NO") String searchOption,
			@RequestParam(name = "search", defaultValue = "") String search) {
		TeamBoardDTO dto = teamBoardService.teamBoardView(team_board_id);
		// vote_id, schedule_id 없으면 null로 나옴
		model.addAttribute("dto", dto);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("ca", category);
		model.addAttribute("ps", pageSize);
		model.addAttribute("so", searchOption);
		model.addAttribute("search", search);
		return "/team/board/tbview";
	}

	@GetMapping("/modify")
	public String tbmodify(Model model, @RequestParam("tb_id") Long team_board_id,
			@ModelAttribute("team_id") Long team_id, @ModelAttribute("team_member_id") Long team_member_id) {
		TeamBoardDTO dto = teamBoardService.teamBoardView(team_board_id);
		model.addAttribute("dto", dto);
		return "/team/board/tbmodify";
	}

	@PostMapping("/modify")
	public String tbmodify(TeamBoardDTO dto) {
		teamBoardService.teamBoardUpdate(dto);
		return "redirect:/team/board/view?tb_id=" + dto.getTeam_board_id();
	}

	@ResponseBody
	@PostMapping("/delete")
	public String tbdelete(@RequestParam("team_id") Long team_id, @RequestParam("tb_id") Long team_board_id,
			@RequestParam(name = "vote_id", defaultValue = "0") Long vote_id) {
		if(vote_id != 0) {
			voteService.voteDelete(vote_id);
		}
		teamBoardService.teamBoardDelete(team_board_id);
		return "/team/board/list?team_id=" + team_id;
	}
}
