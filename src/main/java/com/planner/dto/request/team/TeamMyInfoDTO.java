package com.planner.dto.request.team;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class TeamMyInfoDTO {
	private Long team_id;
	private String team_name;
	private Long member_id;
	private String tm_grade;
	private String tm_nickname;
	private LocalDateTime tm_reg;
}
