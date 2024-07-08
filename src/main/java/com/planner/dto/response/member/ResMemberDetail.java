package com.planner.dto.response.member;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class ResMemberDetail {

	private Long member_id;				// 유저 시퀀스
	
	private String member_email;		// 유저 이메일
	
	private String member_name;		// 유저명
	
	private String member_password;	// 회원 비밀번호
	
	private LocalDate member_birth;		// 유저 생일
	
	private String member_phone;		// 휴대폰번호
	
	private String member_gender;	// 성별
	
	private String member_status;		// 회원상태
	
	private String oauth_id;				// 소셜고유번호
	
	private String oauth_type;			// 소셜로그인 타입
	
	private String member_role;		// 회원권한
	
}
