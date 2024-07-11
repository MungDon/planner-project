package com.planner.dto.request.team;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TeamMemberDTO {
	private Long team_member_id;
	private Long member_id;
	private Long team_id;
	private String tm_grade;
	private LocalDateTime tm_reg;
	private String tm_nickname;
}
