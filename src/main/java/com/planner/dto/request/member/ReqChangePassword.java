package com.planner.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqChangePassword {
	
	private Long member_id; // 회원시퀀스
	
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{4,12}$",message = "비밀번호는 최소 4자에서 12자까지, 영문자, 숫자 및 특수 문자를 포함해야합니다.")
	private String newPassword;		// 새로운 비밀번호
	
	@NotBlank(message = "비밀번호는 재확인은 필수입니다.")
	private String newPassword2;	// 새로운 비밀번호 2
}
