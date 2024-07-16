package com.planner.dto.request.team.vote;

import java.util.List;

import lombok.Getter;

@Getter
public class VoteItemDTO {
	private Long vote_item_id;
	private String vote_item_name;
	private List<Long> vote_members; // team_member_id 담은 List
}
