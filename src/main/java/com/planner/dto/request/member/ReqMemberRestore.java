package com.planner.dto.request.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqMemberRestore {

	private String currentEmail; 		// 현재 등록된 이메일
	
	private String currentPassword;	// 현재 비밀번호
	
	private String oauth_type;			// 소셜 로그인 종류
}
