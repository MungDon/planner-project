package com.planner.dto.request.member;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqOAuth2MemberAdd {

	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "이메일 형식에 맞게 써주세요!")
	private String member_email;			// 이메일
	
	private String member_password;	// 비밀번호(랜덤번호)
	
	private String member_name;			// 유저명
	
	private String oauth_id;					// 소셜 고유번호
	
	private String member_status;			// 가입상태
	
	private String oauth_type;				// 소셜로그인 종류
	
	private String member_role;			// 회원 권한
}
