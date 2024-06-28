package com.planner.dto.request.team.board;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReplyViewDTO {
	private long reply_id;
	private long reply_group;
	private String reply_content;
	private LocalDateTime reply_reg;
	private long team_member_id;
	private String tm_nickname;
}
