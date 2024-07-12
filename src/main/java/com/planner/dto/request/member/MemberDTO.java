package com.planner.dto.request.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.planner.enums.MemberRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
	private Long member_id;						// 회원 시퀀스
	
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(regexp = "^(?=(.*[a-z]){5,})(?=.*[!@])(?=.*\\d).{7,}$",message = "비밀번호는 최소 5개의 영어 소문자와 하나 이상의 특수기호(!, @), 숫자를 포함해야하고 7글자 이상이어야합니다.")
	private String member_password;			// 비밀번호
	
	@NotBlank(message = "이름은 필수입니다.")
	private String member_name;				// 이름
	
	@NotNull(message = "생년월일은 필수입니다.")
	private LocalDate member_birth;			// 생년월일
	
	@NotBlank(message = "이메일은 필수입니다.")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "이메일 형식에 맞게 써주세요!")
	private String member_email;					// 이메일
	
	@NotBlank(message = "전화번호는 필수입니다.")
	@Size(min = 11,max = 11, message = "전화번호를 올바르게 입력하세요")
	private String member_phone;				// 전화번호
	
	@NotBlank(message = "성별은 필수입니다.")
	private String member_gender;				// 성별		(남: M, 여: W)
	
	private LocalDateTime member_reg;		// 가입일시	(default: sysdate)
	private String member_status;				// 회원상태	(기본: b, 탈퇴: d, 정지: j)
	private String friend_request_status;		// 친구신청 상태 리스트 (요청 : R, 친구 : F)
	private String member_role;					// 회원권한

	public void setUserDefaults(PasswordEncoder passwordEncoder) {
		this.member_role = MemberRole.USER.getType();
		this.member_password = passwordEncoder.encode(this.member_password);
	}
}