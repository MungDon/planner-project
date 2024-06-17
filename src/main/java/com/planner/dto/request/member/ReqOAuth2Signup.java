package com.planner.dto.request.member;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqOAuth2Signup {

	private	String member_userid;		// 유저 id
	
	private	LocalDate member_birth;	// 유저 생일
	
	private	String member_phone;		// 유저 폰번호
	
	private	String member_gender;	// 유저 성별
}