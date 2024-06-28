package com.planner.dto.request.team.vote;

import lombok.Data;

@Data
public class VoteMemberDTO {
	private Long vote_item_id;
	private Long team_member_id;
	private String tm_nickname;
}
