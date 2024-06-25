package com.planner.dto.request.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MemberDTO {
	private Long member_id;					// 회원 시퀀스
	private String member_password;			// 비밀번호
	private String member_name;				// 이름
	private LocalDate member_birth;			// 생년월일
	private String member_email;			// 이메일
	private String member_phone;			// 전화번호
	private String member_gender;			// 성별		(남: M, 여: W)
	private LocalDateTime member_reg;		// 가입일시	(default: sysdate)
	private String member_status;			// 회원상태	(기본: b, 탈퇴: d, 정지: j)
	private String friend_request_status;	// 친구신청 상태 리스트 (요청 : R, 친구 : F)
	private String member_role;					// 회원권한

}