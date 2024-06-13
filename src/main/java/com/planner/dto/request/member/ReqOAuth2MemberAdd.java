package com.planner.dto.request.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqOAuth2MemberAdd {

	private String member_email;			// 이메일
	
	private String member_password;	// 비밀번호(랜덤번호)
	
	private String member_name;			// 유저명(이메일과같음)
	
	private String oauth_id;					// 소셜 고유번호
	
	private String member_status;			// 가입상태
}
