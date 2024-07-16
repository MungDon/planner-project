package com.planner.dto.request.team.vote;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class VoteMemberInsertDTO {
	private Long vote_id;
	private Long vote_item_id;
	private Long team_member_id;
	private LocalDateTime vote_end;
}
