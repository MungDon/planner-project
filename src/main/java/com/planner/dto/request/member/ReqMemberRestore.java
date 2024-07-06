package com.planner.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqMemberRestore {
	
	@NotBlank(message = "이메일은 필수입니다.")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "이메일 형식에 맞게 써주세요!")
	private String currentEmail; 		// 현재 등록된 이메일
	
	@NotBlank(message = "비밀번호는 필수입니다.")
	private String currentPassword;	// 현재 비밀번호
	
	private String oauth_type;			// 소셜 로그인 종류
}
