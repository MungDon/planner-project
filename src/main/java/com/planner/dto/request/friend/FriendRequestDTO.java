package com.planner.dto.request.friend;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FriendRequestDTO {
	private Long friend_request_id;			// 친구신청 시퀀스
	private Long member_receive_id;			// 받는 (친구)시퀀스
	private Long member_send_id;			// 보낸 (나의)시퀀스
	private String friend_request_status;	// 친구 상태 (보냄 : S, 받음 : R)
	private String member_email;			// 친구 이메일 / DB에 없음
}