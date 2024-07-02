package com.planner.dto.request.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqChangePassword {
	
	private Long member_id; // 회원시퀀스
	
	private String newPassword;		// 새로운 비밀번호
	
	private String newPassword2;	// 새로운 비밀번호 2
}
