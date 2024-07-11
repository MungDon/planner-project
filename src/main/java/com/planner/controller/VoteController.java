package com.planner.controller;

import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.planner.dto.request.team.vote.VoteInfoDTO;
import com.planner.dto.request.team.vote.VoteItemDTO;
import com.planner.dto.request.team.vote.VoteMemberInsertDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.service.TeamMemberService;
import com.planner.service.VoteService;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vote")
@PreAuthorize("isAuthenticated()")
public class VoteController {

	private final VoteService voteService;
	private final TeamMemberService tmService;

	// 투표 정보 출력
	@PostMapping("/info")
	public String voteInfo(Model model, @UserData ResMemberDetail detail, 
							@RequestParam("vote_id") Long vote_id,
							@RequestParam("team_id") Long team_id) {
		VoteInfoDTO dto = voteService.voteInfo(vote_id);
		Long team_member_id = tmService.teamMemberId(team_id, detail.getMember_id());
		// 투표 한적 있으면 1 아니면 0
		long voted = 0;
		int total_vote = 0;
		for(VoteItemDTO item : dto.getVote_items()) {
			if(voted == 0) {
				voted = item.getVote_members().stream()
						.filter(t -> t.getTeam_member_id() == team_member_id)
						.count();
			}
			total_vote += item.getVote_members().size();
		}
		boolean end = LocalDateTime.now().isAfter(dto.getVote_end());
		model.addAttribute("dto", dto);
		model.addAttribute("voted", voted);
		model.addAttribute("total_vote", total_vote);
		model.addAttribute("end", end);
		return "/team/board/vote";
	}

	// 투표했을 때 vote_member에 추가
	@PostMapping("/memberinsert")
	@ResponseBody
	public int voteMemberInsert(@RequestBody VoteMemberInsertDTO dto) {
		int result = voteService.voteMemberInsert(dto);
		return result;
	}

}
