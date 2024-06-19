package com.planner.dto.response.member;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class ResMemberDetail {

	private String member_email;		// 유저 이메일
	
	private String member_name;		// 유저명
	
	private LocalDate member_birth;		// 유저 생일
	
	private String member_phone;		// 휴대폰번호
	
	private String member_gender;	// 성별
	
}
