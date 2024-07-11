package com.planner.dto.request.team;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamDTO {
	private Long team_id;
	private String team_name;
	private String team_explain;
	private String team_image;
	
	public void setTeam_id(Long team_id) {
		this.team_id = team_id;
	}

	public void setTeam_image(String team_image) {
		this.team_image = team_image;
	}
}
