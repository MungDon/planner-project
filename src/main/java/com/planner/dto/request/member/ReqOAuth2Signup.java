package com.planner.dto.request.member;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqOAuth2Signup {
	
	private String member_id;				// 회원ㄴ 고유번호
	
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "이메일 형식에 맞게 써주세요!")
	private String member_email;		// 회원이메일
	
	@NotBlank(message = "생년월일은 필수입니다.")
	private	LocalDate member_birth;	// 유저 생일
	
	@NotBlank(message = "전화번호는 필수입니다.")
	@Size(min = 11,max = 11, message = "전화번호를 올바르게 입력하세요")
	private	String member_phone;		// 유저 폰번호
	
	@NotBlank(message = "성별은 필수입니다.")
	private	String member_gender;	// 유저 성별
	
	private String member_status;		// 회원상태
}
