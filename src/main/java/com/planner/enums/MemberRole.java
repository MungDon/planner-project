package com.planner.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

	SUPER_ADMIN("ROLE_SUPER_ADMIN","최고관리자"),
	USER("ROLE_USER","일반회원");
	
	private final String type;
	private final String name;
}
