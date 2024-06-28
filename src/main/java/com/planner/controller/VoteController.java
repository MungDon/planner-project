package com.planner.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.request.team.vote.VoteInfoDTO;
import com.planner.dto.request.team.vote.VoteMemberDTO;
import com.planner.service.VoteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vote")
public class VoteController {

	private final VoteService voteService;
	
	@PostMapping("/info")
	public String voteInfo(Model model, @RequestParam("vote_id")Long vote_id) {
		VoteInfoDTO dto = voteService.voteInfo(vote_id);
		model.addAttribute("dto", dto);
		return "/team/board/vote";
	}
	
	@PostMapping("/memberlist")
	@ResponseBody
	public List<VoteMemberDTO> voteMemberList(@RequestParam("vote_id")Long vote_id) {
		List<VoteMemberDTO> list = voteService.voteMemberList(vote_id);
		return list;
	}
	
	public void voteMemberInsert() {
		
	}
	
	public void voteMemberDelete() {
		
	}

}
