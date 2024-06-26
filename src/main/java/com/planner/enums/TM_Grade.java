package com.planner.enums;

import java.util.Arrays;
import java.util.Set;

import lombok.Getter;

// team_member의 tm_grade 등급(권한) 설정을 위한 enum

@Getter
public enum TM_Grade {
	ROLE_TEAM_MASTER("TEAM_MASTER"),
	ROLE_TEAM_SUB_MASTER("TEAM_SUB_MASTER"),
	ROLE_TEAM_USER("TEAM_USER"),
	ROLE_TEAM_WAIT("WAIT");

	public static final Set<String> grade_set = 
			Set.of(Arrays.stream(TM_Grade.values())
					.map(TM_Grade::getValue)
					.toArray(String[]::new)
					);
	
	private TM_Grade(String value){
		this.value = value;
	}

	private String value;
}
