package com.planner.dto.request.member;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqMemberUpdate {
	
	private Long member_id;				// 유저 시퀀스

	private String member_name;		// 회원명	
	
	private LocalDate member_birth;		// 회원생일
	
	private String member_phone;		// 휴대번호
}
