package com.planner.dto.request.team.vote;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VoteDTO {
	private Long vote_id;
	private String vote_title;
	private String vote_content;
	private LocalDateTime vote_end;
}
