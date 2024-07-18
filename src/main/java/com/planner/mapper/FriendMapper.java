package com.planner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.planner.dto.request.friend.FriendDTO;
import com.planner.dto.request.friend.FriendRequestDTO;

@Mapper
@Repository
public interface FriendMapper {
	
//	친구 시퀀스로 객체 찾기
	public FriendDTO findByFriendId(Long friend_id);
	
//	회원 시퀀스로 친구 시퀀스 찾기
	public Long findByFriendSeq(@Param(value = "member_my_id") Long member_my_id,
								@Param(value = "member_friend_id") Long member_friend_id);
	
//	친구신청 (보냄)
	public void friendRequest(FriendRequestDTO friendRequestDTO);
	
//	친구신청 상태 찾기
	public String friendRequestStatus(@Param("member_receive_id") Long member_receive_id,
							   		  @Param("member_send_id") Long member_send_id);
	
//	(받은)친구신청 갯수
	public int receiveRequestCount(@Param("member_receive_id") Long member_receive_id);
	
//	(받은)친구신청 리스트
	public List<FriendRequestDTO> receiveRequestList(@Param("member_receive_id") Long member_receive_id);
	
//	(보낸)친구신청 리스트
	public List<FriendRequestDTO> sendRequestList(@Param("member_send_id") Long member_send_id);
	
//	친구수락 (친구상태 업데이트)
	public void friendAccept(@Param("member_receive_id") Long member_receive_id,
							 @Param("member_send_id") Long member_send_id);
	
//	친구 테이블에 추가 (friend)
	public void friendAdd(FriendDTO friendDTO);

//	친구신청 취소/거절
	public void requestDelete(@Param("member_receive_id") Long member_receive_id,
							  @Param("member_send_id") Long member_send_id);
	
//	친구목록
	public List<FriendDTO> friendList(@Param("member_my_id") Long member_my_id);
	
//	친구 닉네임 변경
	public void friendNickName(FriendDTO friendDTO);
	
//	친구 별명 변경
	public void friendMemo(FriendDTO friendDTO);
	
//	친구정보 (정방향)
	public FriendDTO friendInfo(FriendDTO friendDTO);
	
//	친구정보 (역방향)
	public FriendDTO friendInfoC(FriendDTO friendDTO);
	
//	친구삭제
	public void friendDelete(@Param("friend_id") Long friend_id);
	
//	친구상태 찾기
	public String friendStatus(@Param("member_receive_id") Long member_receive_id,
			  				   @Param("member_send_id") Long member_send_id);
}