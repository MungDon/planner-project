package com.planner.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {

	BASIC("B","가입"),
	DELETE("D","탈퇴"),
	LOCK("L", "정지"),
	NOT_DONE("N","가입미완료"),
	RESTORE("R", "복구신청");
	
	private final String code;		// 상태코드
	private final String status;	// 상태명
}
