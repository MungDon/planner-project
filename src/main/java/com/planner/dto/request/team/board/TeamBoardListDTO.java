package com.planner.dto.request.team.board;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class TeamBoardListDTO {
	private long team_board_id;
	private long team_member_id;
	private Long vote_id;
	private Long schedule_id;
	private String tb_category;
	private String tb_title;
	private LocalDateTime tb_reg;
	private String tm_nickname;
	private Integer reply_count;
}
