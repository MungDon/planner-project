package com.planner.dto.request.team.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamBoardUpdateDTO {
	private Long team_board_id;
	private String tb_category;
	private String tb_title;
	private String tb_content;
	private Long team_member_id;
}
