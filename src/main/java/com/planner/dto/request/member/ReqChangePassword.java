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
	@Pattern(regexp = "^(?=(.*[a-z]){5,})(?=.*[!@])(?=.*\\d).{7,}$",message = "비밀번호는 최소 5개의 영어 소문자와 하나 이상의 특수기호(!, @), 숫자를 포함해야하고 7글자 이상이어야합니다.")
	private String newPassword;		// 새로운 비밀번호
	
	@NotBlank(message = "비밀번호는 재확인은 필수입니다.")
	private String newPassword2;	// 새로운 비밀번호 2
}
