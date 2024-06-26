package com.planner.service;

import java.security.Principal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.planner.dto.request.friend.FriendDTO;
import com.planner.dto.request.friend.FriendRequestDTO;
import com.planner.dto.request.member.MemberDTO;
import com.planner.mapper.FriendMapper;
import com.planner.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

	private final FriendMapper friendMapper;
	private final MemberMapper memberMapper;
	
//	시퀀스 받아서 객체 찾기
	public FriendRequestDTO findByFriendRequest(Long member_id) {
		return friendMapper.findByFriendRequest(member_id);
	}
	
//	친구찾기 리스트
	public List<FriendDTO> findFriend(@Param("member_my_id") Long member_my_id) {
		return friendMapper.findFriend(member_my_id);
	}
	
//	친구신청 (보냄)
	public void friendRequest(Long member_id, String member_email) {	// member_id : 친구(신청 받은) 시퀀스
		FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
		Long myId = memberMapper.findByMemberId(member_email);	// 나의(보낸) 시퀀스
		
		friendRequestDTO.setMember_receive_id(member_id);				// 내가 친구신청 보낸 친구의 시퀀스
		friendRequestDTO.setMember_send_id(myId);						// 나의 시퀀스
		
		friendMapper.friendRequest(friendRequestDTO);					// 친구신청 void 메서드
	}
	
//	친구신청 상태 찾기
	public String friendRequestStatus(@Param("member_receive_id") Long member_receive_id,
							   		  @Param("member_send_id") Long member_send_id) {
		return friendMapper.friendRequestStatus(member_receive_id, member_send_id);
	}
	
//	(받은)친구신청 갯수
	public int receiveRequestCount(String member_email) {
		Long myId = memberMapper.findByMemberId(member_email);
		
		return friendMapper.receiveRequestCount(myId);
	}
	
//	(받은)친구신청 리스트
	public List<FriendRequestDTO> receiveRequestList(String member_email) {
		Long myId = memberMapper.findByMemberId(member_email);
		List<FriendRequestDTO> list = friendMapper.receiveRequestList(myId);
		
//		for (FriendRequestDTO friendRequestDTO : list) {
//			if (friendRequestDTO.getMember_send_id() != null) {
//				String email = memberMapper.findByMemberEmail(friendRequestDTO.getMember_send_id());
//				friendRequestDTO.setMember_email(email);
//			}
//		}
		return list;
	}
	
//	(보낸)친구신청 리스트
	public List<FriendRequestDTO> sendRequestList(String member_email) {
		Long myId = memberMapper.findByMemberId(member_email);
		List<FriendRequestDTO> list = friendMapper.sendRequestList(myId);
		
		return list;
	}
	
//	친구신청 취소/거절
	public void requestDelete(Long member_receive_id, Long member_send_id) {
		friendMapper.requestDelete(member_receive_id, member_send_id);
	}
	
//	(보낸)친구신청 취소
//	public void sendDelete(@Param("member_receive_id") Long member_receive_id,
//			  			   Principal principal) {
//		Long myId = memberMapper.findByMemberId(principal.getName());
//		friendMapper.sendDelete(member_receive_id, myId);
//	}
	
//	친구수락 (+친구상태 업데이트)
	public void friendAccept(String member_email, Long member_send_id) {
		Long myId = memberMapper.findByMemberId(member_email);				// 나의(받은) 시퀀스
		MemberDTO memberMyDTO = memberMapper.findByMemberSeq(myId);					// 나의 객체
		MemberDTO memberFriendDTO = memberMapper.findByMemberSeq(member_send_id);	// 친구 객체
		FriendDTO friendDTO = new FriendDTO();
		
		friendDTO.setMember_my_id(myId);
		friendDTO.setMember_friend_id(member_send_id);
		friendDTO.setFriend_my_nickname(memberMyDTO.getMember_name());				// 나의 이름
		friendDTO.setFriend_nickname(memberFriendDTO.getMember_name());				// 친구 이름
		
		friendMapper.friendAccept(myId, member_send_id);				// 친구 상태 업데이트 메서드
		friendMapper.friendAdd(friendDTO);								// 친구 테이블에 추가 메서드
	}
	
//	친구목록 (myId != member_my_id : 'C' / 나, 친구 위치 바꿔서 set 해줌 / friend_status = 'C')
	public List<FriendDTO> friendList(String member_email) {
		long myId = memberMapper.findByMemberId(member_email);
		
		List<FriendDTO> list = friendMapper.friendList(myId);
		
		for (FriendDTO friendDTO : list) {
			if (!friendDTO.getMember_my_id().equals(myId)) {		// 'C' 역방향 상태 / 나의 정보와 친구 정보의 위치가 바뀜
				// 값의 위치를 바꿔주기 위해 변수에 대입
				String friendEmail = memberMapper.findByMemberEmail(friendDTO.getMember_my_id());	// my_id = 친구 시퀀스
				Long member_my_id = friendDTO.getMember_my_id();
				Long member_friend_id = friendDTO.getMember_friend_id();
				String friend_my_nickname = friendDTO.getFriend_my_nickname();
				String friend_nickname = friendDTO.getFriend_nickname();
				String friend_my_memo = friendDTO.getFriend_my_memo();
				String friend_memo = friendDTO.getFriend_memo();
				
				// 나의 정보에 친구 정보를, 친구 정보에 내 정보를 대입
				friendDTO.setMember_my_id(member_friend_id);
				friendDTO.setMember_friend_id(member_my_id);
				friendDTO.setFriend_my_nickname(friend_nickname);
				friendDTO.setFriend_nickname(friend_my_nickname);
				friendDTO.setFriend_my_memo(friend_memo);
				friendDTO.setFriend_memo(friend_my_memo);
				friendDTO.setFriend_status("C");			// 바뀐 정보임을 알려주는 변수 	/ DB에 없음
				friendDTO.setMember_email(friendEmail);		// 친구 이메일 				/ DB에 없음
			}else {											// 'B' 정방향 상태
				String friendEmail = memberMapper.findByMemberEmail(friendDTO.getMember_friend_id());
				friendDTO.setMember_email(friendEmail);		// 친구 이메일 				/ DB에 없음
				friendDTO.setFriend_status("B"); 			// 정방향으로 저장된 정보		/ DB에 없음
			}
		}
		return list;
	}
	
//	친구 닉네임 변경
	public void friendNickName(FriendDTO frndDTO) {
		FriendDTO friendDTO = new FriendDTO();
		if (frndDTO.getFriend_status().equals("B")) {			// 정방향 배치일 때
			friendDTO.setFriend_id(frndDTO.getFriend_id());
			friendDTO.setFriend_my_nickname(frndDTO.getFriend_my_nickname());
			friendDTO.setFriend_nickname(frndDTO.getFriend_nickname());
		}else if (frndDTO.getFriend_status().equals("C")) {		// 역방향 배치일 때
			// 값의 위치를 바꿔주기 위해 변수에 대입
			String friend_my_nickname = frndDTO.getFriend_my_nickname();
			String friend_nickname = frndDTO.getFriend_nickname();
			
			friendDTO.setFriend_id(frndDTO.getFriend_id());
			friendDTO.setFriend_my_nickname(friend_nickname);
			friendDTO.setFriend_nickname(friend_my_nickname);
		}
		friendMapper.friendNickName(friendDTO);					// 닉네임 변경 메서드
	}
	
//	친구 매모 변경
	public void friendMemo(FriendDTO frndDTO) {
		FriendDTO friendDTO = new FriendDTO();
		
		if (frndDTO.getFriend_status().equals("B")) {			// 정방향 배치일 때
			friendDTO.setFriend_id(frndDTO.getFriend_id());
			friendDTO.setFriend_my_memo(frndDTO.getFriend_my_memo());
			friendDTO.setFriend_memo(frndDTO.getFriend_memo());
		}else if (frndDTO.getFriend_status().equals("C")) {		// 역방향 배치일 때
			// 값의 위치를 바꿔주기 위해 변수에 대입
			String friend_my_memo = frndDTO.getFriend_my_memo();
			String friend_memo = frndDTO.getFriend_memo();
			
			friendDTO.setFriend_id(frndDTO.getFriend_id());
			friendDTO.setFriend_my_memo(friend_memo);
			friendDTO.setFriend_memo(friend_my_memo);
		}
		friendMapper.friendMemo(friendDTO);						// 메모 변경 메서드
	}

//	친구정보
	public FriendDTO friendInfo(FriendDTO frndDTO) {
		FriendDTO friendDTO = new FriendDTO();
		if (frndDTO.getFriend_status().equals("B")) {			// 정방향 배치일 때
			friendDTO.setMember_my_id(frndDTO.getMember_my_id());
			friendDTO.setMember_friend_id(frndDTO.getMember_friend_id());
			
			return friendMapper.friendInfo(friendDTO);
		}else if (frndDTO.getFriend_status().equals("C")) {		// 역방향 배치일 때
			// 값의 위치를 바꿔주기 위해 변수에 대입
			Long member_my_id = frndDTO.getMember_my_id();
			Long member_friend_id = frndDTO.getMember_friend_id();
						
			friendDTO.setMember_my_id(member_friend_id);
			friendDTO.setMember_friend_id(member_my_id);
			
			return friendMapper.friendInfoC(friendDTO);			// 역방향일 경우 join 문 변경됨 / on f.member_my_id = m.member_id
		}else {
			throw new IllegalArgumentException();
		}
	}
	
//	친구삭제
	public void friendDelete(Long friend_id, Long member_my_id, Long member_friend_id) {
		Long member_receive_id = member_my_id;
		Long member_send_id = member_friend_id;
		friendMapper.friendDelete(friend_id);
		friendMapper.requestDelete(member_receive_id, member_send_id);
	}
}