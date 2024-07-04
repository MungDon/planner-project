package com.planner.dto.request.team.vote;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class VoteInfoDTO {
	private long vote_id;
	private String vote_title;
	private String vote_content;
	private LocalDateTime vote_end;
	private List<VoteItemDTO> vote_items;
}
