package com.planner.dto.request.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqPasswordChk {

	private String member_password;	// 비밀번호
	
	private String url;							// 주소
}
