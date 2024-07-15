package com.planner.dto.request.member;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqMemberUpdate {
	
	private Long member_id;				// 유저 시퀀스

	@NotBlank(message = "이름은 필수입니다.")
	@Pattern(regexp = "^[가-힣]+$",message = "한글이름만 작성해주세요. 영어일경우 발음대로 적으세요")
	private String member_name;		// 회원명	
	
	@NotNull(message = "생년월일 필수입니다.")
	private LocalDate member_birth;		// 회원생일
	
	@NotBlank(message = "전화번호는 필수입니다.")
	@Pattern(regexp = "^([0-9]{11})$",message = "전화번호는 숫자만 입력가능합니다.")
	private String member_phone;		// 휴대번호
}
