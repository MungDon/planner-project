package com.planner.dto.request.member;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.planner.enums.MemberRole;
import com.planner.enums.MemberStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqOAuth2Signup {
	
	private Long member_id;				// 회원 고유번호
	
	private String member_email;			// 회원이메일
	
	@NotNull(message = "생년월일은 필수입니다.")
	private	LocalDate member_birth;	// 유저 생일
	
	@NotBlank(message = "전화번호는 필수입니다.")
	@Pattern(regexp = "^([0-9]{11})$",message = "전화번호는 숫자만 입력가능합니다.")
	private	String member_phone;		// 유저 폰번호
	
	@NotBlank(message = "성별은 필수입니다.")
	private	String member_gender;	// 유저 성별
	
	private String member_status;		// 회원상태
	
	private String member_role; 		// 회원권한
	
	public void setUserRoles() {
		this.member_role = MemberRole.USER.getType();
		this.member_status = MemberStatus.BASIC.getCode();
	}
}
