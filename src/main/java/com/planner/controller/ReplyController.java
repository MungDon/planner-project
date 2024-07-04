package com.planner.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.planner.dto.request.team.board.ReplyDTO;
import com.planner.dto.request.team.board.ReplyViewDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.service.ReplyService;
import com.planner.service.TeamMemberService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reply")
@PreAuthorize("isAuthenticated()")
public class ReplyController {

	private final ReplyService replyService;
	private final TeamMemberService tmService;
	
	@PostMapping("/insert")
	@ResponseBody
	public int replyInsert(ReplyDTO dto, @UserData ResMemberDetail detail,
			@RequestParam("team_id")long team_id) {
		dto.setReply_reg(LocalDateTime.now());
		dto.setTeam_member_id(tmService.teamMemberId(team_id, detail.getMember_id()));
		return replyService.replyInsert(dto);
	}
	
	@GetMapping("/list")
	public String replyList(Model model, @RequestParam("team_board_id")long team_board_id,
							@RequestParam("team_id")long team_id, @UserData ResMemberDetail detail) {
		List<ReplyViewDTO> replyList = replyService.replyList(team_board_id);
		model.addAttribute("replyList", replyList);
		if(replyList.size() > 0) {
			String tm_grade = tmService.teamMemberGrade(team_id, detail.getMember_id());
			long team_member_id = tmService.teamMemberId(team_id, detail.getMember_id());
			model.addAttribute("tm_grade", tm_grade);
			model.addAttribute("team_member_id", team_member_id);
		}
		return "/team/board/reply";
	}
	
	@DeleteMapping("/delete")
	@ResponseBody()
	public String replyDelete(@RequestParam("reply_id")long reply_id) {
		replyService.replyDelete(reply_id);
		return HttpStatus.OK.toString();
	}
	
	@PatchMapping("/modify")
	@ResponseBody()
	public String replyModify(@RequestParam("reply_content")String reply_content,
							@RequestParam("reply_id")long reply_id) {
		replyService.replyUpdate(reply_content, reply_id);
		return HttpStatus.OK.toString();
	}
}
