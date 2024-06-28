package com.planner.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.planner.service.ReplyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

	private final ReplyService replyService;
	
	@PostMapping("/insert")
	@ResponseBody
	public int replyInsert(ReplyDTO dto) {
		dto.setReply_reg(LocalDateTime.now());
		return replyService.replyInsert(dto);
	}
	
	@GetMapping("/list")
	public String replyList(Model model, @RequestParam("team_board_id")long team_board_id) {
		List<ReplyViewDTO> replyList = replyService.replyList(team_board_id);
		model.addAttribute("replyList", replyList);
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
