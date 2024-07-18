package com.planner.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.request.friend.FriendDTO;
import com.planner.dto.request.friend.FriendRequestDTO;
import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.exception.CustomException;
import com.planner.exception.ErrorCode;
import com.planner.mapper.FriendMapper;
import com.planner.mapper.MemberMapper;
import com.planner.util.CommonUtils;
import com.planner.util.UserData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

	private final FriendMapper friendMapper;
	private final MemberMapper memberMapper;
	
//	회원 시퀀스로 친구 시퀀스 찾기
	@Transactional(readOnly = true)
	public Long findByFriendSeq(Long member_my_id , Long member_friend_id) {
		Long friend_id = friendMapper.findByFriendSeq(member_my_id, member_friend_id);
		
		return friend_id;
	}
	
//	친구신청 (보냄)
	@Transactional
	public void friendRequest(Long member_id, Long myId) {	// member_id : 친구(신청 받은) 시퀀스
		FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
		if (CommonUtils.isEmpty(member_id)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		
		friendRequestDTO.setMember_receive_id(member_id);				// 내가 친구신청 보낸 친구의 시퀀스
		friendRequestDTO.setMember_send_id(myId);						// 나의 시퀀스
		
		friendMapper.friendRequest(friendRequestDTO);					// 친구신청 void 메서드
	}
	
//	친구신청 상태 찾기
	@Transactional(readOnly = true)
	public String friendRequestStatus(@Param("member_receive_id") Long member_receive_id,
							   		  @Param("member_send_id") Long member_send_id) {
		return friendMapper.friendRequestStatus(member_receive_id, member_send_id);
	}
	
//	(받은)친구신청 갯수
	@Transactional(readOnly = true)
	public int receiveRequestCount(Long member_id) {
		if (CommonUtils.isEmpty(member_id)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		
		return friendMapper.receiveRequestCount(member_id);
	}
	
//	(받은)친구신청 리스트
	@Transactional(readOnly = true)
	public List<FriendRequestDTO> receiveRequestList(Long member_id) {
		if (CommonUtils.isEmpty(member_id)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		List<FriendRequestDTO> list = friendMapper.receiveRequestList(member_id);
		
		return list;
	}
	
//	(보낸)친구신청 리스트
	@Transactional(readOnly = true)
	public List<FriendRequestDTO> sendRequestList(Long member_id) {
		if (CommonUtils.isEmpty(member_id)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		List<FriendRequestDTO> list = friendMapper.sendRequestList(member_id);
		
		return list;
	}
	
//	친구신청 취소/거절
	@Transactional
	public void requestDelete(Long member_receive_id, Long member_send_id) {
		if (CommonUtils.isEmpty(member_receive_id) || CommonUtils.isEmpty(member_send_id)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		friendMapper.requestDelete(member_receive_id, member_send_id);
	}
	
//	친구수락 (+친구상태 업데이트)
	@Transactional
	public void friendAccept(Long member_receive_id, Long member_send_id) {
		if (CommonUtils.isEmpty(member_send_id)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		MemberDTO memberMyDTO = memberMapper.findByMemberSeq(member_receive_id);	// 나의 객체
		MemberDTO memberFriendDTO = memberMapper.findByMemberSeq(member_send_id);	// 친구 객체
		FriendDTO friendDTO = new FriendDTO();
		
		friendDTO.setMember_my_id(member_receive_id);
		friendDTO.setMember_friend_id(member_send_id);
		friendDTO.setFriend_my_nickname(memberMyDTO.getMember_name());				// 나의 이름
		friendDTO.setFriend_nickname(memberFriendDTO.getMember_name());				// 친구 이름
		
		friendMapper.friendAccept(member_receive_id, member_send_id);			// 친구 상태 업데이트 메서드
		friendMapper.friendAdd(friendDTO);											// 친구 테이블에 추가 메서드
	}
	
//	친구목록 (myId != member_my_id : 'C' / 나, 친구 위치 바꿔서 set 해줌 / friend_status = 'C')
	@Transactional(readOnly = true)
	public List<FriendDTO> friendList(ResMemberDetail detail) {
		if (CommonUtils.isEmpty(detail.getMember_email())) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		List<FriendDTO> list = friendMapper.friendList(detail.getMember_id());
		
		for (FriendDTO friendDTO : list) {
			if (!friendDTO.getMember_my_id().equals(detail.getMember_id())) {		// 'C' 역방향 상태 / 나의 정보와 친구 정보의 위치가 바뀜
				// 값의 위치를 바꿔주기 위해 변수에 대입
				String friendEmail = memberMapper.findEmailBySeq(friendDTO.getMember_my_id());	// my_id = 친구 시퀀스
				String friendName = memberMapper.findNameBySeq(friendDTO.getMember_my_id());
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
				friendDTO.setFriend_status("C");			// 바뀐 정보임을 알려주는 변수	/ DB에 없음
				friendDTO.setMember_email(friendEmail);		// 친구 이메일 				/ DB에 없음
				friendDTO.setMember_name(friendName);		// 친구 이름 				/ DB에 없음
			}else {											// 'B' 정방향 상태
				String friendEmail = memberMapper.findEmailBySeq(friendDTO.getMember_friend_id());
				String friendName = memberMapper.findNameBySeq(friendDTO.getMember_friend_id());
				friendDTO.setMember_email(friendEmail);		// 친구 이메일 				/ DB에 없음
				friendDTO.setMember_name(friendName);		// 친구 이름					/ DB에 없음
				friendDTO.setFriend_status("B"); 			// 정방향으로 저장된 정보		/ DB에 없음
			}
		}
		return list;
	}
	
//	친구 닉네임 변경
	@Transactional
	public void friendNickName(FriendDTO frndDTO) {
		if (CommonUtils.isEmpty(frndDTO)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
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
	
//	친구 메모 변경
	@Transactional
	public void friendMemo(FriendDTO frndDTO) {
		if (CommonUtils.isEmpty(frndDTO)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		
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
		friendMapper.friendMemo(friendDTO);		// 메모 변경 메서드
	}

//	친구정보
	@Transactional(readOnly = true)
	public FriendDTO friendInfo(Long friend_id, String friend_status) {
		if (CommonUtils.isEmpty(friend_id)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		
		FriendDTO frndDTO;		// 나의 시퀀스와 친구 시퀀스를 배치하기위한 객체
		FriendDTO friendDTO = new FriendDTO();	// 리턴 할 최종 객체
		if (friend_status != null) {
			frndDTO = friendMapper.findByFriendId(friend_id);
			if (friend_status.equals("B")) {			// 정방향 배치일 때
				friendDTO = friendMapper.friendInfo(frndDTO);
				friendDTO.setMember_my_id(frndDTO.getMember_my_id());
				friendDTO.setMember_friend_id(frndDTO.getMember_friend_id());
				friendDTO.setFriend_status("B");
				
				return friendDTO;
			}else if (friend_status.equals("C")) {		// 역방향 배치일 때
				// 값의 위치를 바꿔주기 위해 변수에 대입
				Long member_my_id = frndDTO.getMember_my_id();
				Long member_friend_id = frndDTO.getMember_friend_id();
							
				friendDTO.setMember_my_id(member_friend_id);
				friendDTO.setMember_friend_id(member_my_id);
				
				friendDTO = friendMapper.friendInfoC(frndDTO);
				friendDTO.setFriend_status("C");
				
				return friendDTO;			// 역방향일 경우 join 문 변경됨 / on f.member_my_id = m.member_id
			}
		}else {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		return friendDTO;
	}
	
//	친구삭제
	@Transactional
	public void friendDelete(Long friend_id, Long member_my_id, Long member_friend_id) {
		if (CommonUtils.isEmpty(member_my_id) || CommonUtils.isEmpty(member_friend_id)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		
		Long member_receive_id = member_my_id;
		Long member_send_id = member_friend_id;
		friendMapper.friendDelete(friend_id);
		friendMapper.requestDelete(member_receive_id, member_send_id);
	}
	
//	친구상태 찾기
	@Transactional(readOnly = true)
	public String friendStatus(Long member_receive_id, Long member_send_id) {
		return friendMapper.friendStatus(member_receive_id, member_send_id);
	}
}