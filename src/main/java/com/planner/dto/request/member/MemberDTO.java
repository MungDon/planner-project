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
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{4,12}$",message = "비밀번호는 최소 4자에서 12자까지, 영문자, 숫자 및 특수 문자를 포함해야합니다.")
	private String member_password;			// 비밀번호
	
	@NotBlank(message = "이름은 필수입니다.")
	@Pattern(regexp = "^[가-힣]+$",message = "한글이름만 작성해주세요. 영어일경우 발음대로 적으세요")
	private String member_name;				// 이름
	
	@NotNull(message = "생년월일은 필수입니다.")
	private LocalDate member_birth;			// 생년월일
	
	@NotBlank(message = "이메일은 필수입니다.")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "이메일 형식에 맞게 써주세요!")
	private String member_email;					// 이메일
	
	@NotBlank(message = "전화번호는 필수입니다.")
	@Pattern(regexp = "^([0-9]{11})$",message = "전화번호는 숫자만 입력가능합니다.")
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