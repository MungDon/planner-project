package com.planner.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.request.friend.FriendRequestDTO;
import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.request.member.ReqChangePassword;
import com.planner.dto.request.member.ReqMemberRestore;
import com.planner.dto.request.member.ReqMemberUpdate;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberRole;
import com.planner.enums.MemberStatus;
import com.planner.exception.CustomException;
import com.planner.exception.ErrorCode;
import com.planner.exception.RestCustomException;
import com.planner.mapper.FriendMapper;
import com.planner.mapper.MemberMapper;
import com.planner.util.CommonUtils;
import com.planner.util.UserData;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final FriendMapper friendMapper;
	private final PasswordEncoder passwordEncoder;

	// 회원가입
	@Transactional
	public int memberInsert(MemberDTO memberDTO) {
		if (isMember(memberDTO.getMember_email())) {
			throw new CustomException(ErrorCode.ID_DUPLICATE);// 이메일(아이디 중복)에 대한 커스텀예외
		}
		memberDTO.setMember_role(MemberRole.USER.getType());
		memberDTO.setMember_password(passwordEncoder.encode(memberDTO.getMember_password()));
		return memberMapper.memberInsert(memberDTO);
	}

	/* @UserData */
	@Transactional(readOnly = true)
	public ResMemberDetail memberDetail(String member_email) {
		ResMemberDetail detail = memberMapper.findByEmail(member_email);
		return detail;
	}

	/* 소셜로그인시 내 정보가져오기 */
	@Transactional(readOnly = true)
	public ResMemberDetail memberDetailForSocial(String member_email, String oauth_type) {
		ResMemberDetail detail = memberMapper.findByEmailAndOAuthType(member_email, oauth_type);
		return detail;
	}
	
	/*일반로그인회원 정보가져오기*/
	@Transactional(readOnly = true)
	public ResMemberDetail formMember(String toEmail) {
		ResMemberDetail detail = memberMapper.formMember(toEmail);
		return detail;
	}
	

	/* 회원 정보 수정 */
	@Transactional
	public void memberUpdate(ReqMemberUpdate req) {
		memberMapper.memberUpdate(req);
	}

	/* 로그인시 회원 상태 코드 체크 */
	@Transactional(readOnly = true)
	public void memberStatusChk(String statusCode, HttpServletRequest request, HttpServletResponse response) {
		if (statusCode.equals(MemberStatus.DELETE.getCode())) {
			CommonUtils.removeCookiesAndSession(request, response);
			throw new CustomException(ErrorCode.WITHDRAWN_MEMBER);
		}
		if (statusCode.equals(MemberStatus.RESTORE.getCode())) {
			CommonUtils.removeCookiesAndSession(request, response);
			throw new CustomException(ErrorCode.ACCOUNT_RESTORE_PENDING);
		}
	}

	/* 비번체크 */
	public int passwordChk(String currnetPw, ResMemberDetail member) {
		int result = 0;

		// 소셜로그인일때
		if (member != null && !member.getOauth_id().equals("none") && CommonUtils.isEmpty(currnetPw)) {
			return result = 1;
		}

		// 일반로그인일때
		if (member != null && passwordEncoder.matches(currnetPw, member.getMember_password())) {
			return result = 1;
		}
		return result;
	}

	/* 회원 탈퇴 */
	@Transactional
	public void memberDelete(Long member_id) {
		memberMapper.changeMemberStatus(member_id, MemberStatus.DELETE.getCode());
	}

	/* 회원복구시 회원 상태코드별 반환 예외 */
	private void throwForStatus(String status) {
		switch (status) {
		case "R": // 이미 신청상태
			throw new RestCustomException(ErrorCode.REQUEST_DUPLICATE);
		case "B": // 신청대상아님
		case "N":
			throw new RestCustomException(ErrorCode.INELIGIBLE_REQUEST);
		}
	}

	/* 회원 복구 */
	@Transactional
	public int memberRestore(ReqMemberRestore req) {
		int result = 0;
		// 소셜 로그인일때
		if (!CommonUtils.isEmpty(req.getOauth_type())) {
			ResMemberDetail memberDetail = memberMapper.findByEmailAndOAuthType(req.getCurrentEmail(),
					req.getOauth_type());

			// member 값이 없으면 0 반환
			if (CommonUtils.isEmpty(memberDetail)) {
				return result;
			}

			// member_status 별 숫자 코드 반환
			throwForStatus(memberDetail.getMember_status());

			// 상태코드 변경(R)
			result = memberMapper.changeMemberStatus(memberDetail.getMember_id(), MemberStatus.RESTORE.getCode());
			return result;
		}

		// 일반로그인일때
		if (CommonUtils.isEmpty(req.getOauth_type())) {
			try {
				ResMemberDetail memberDetail = memberMapper.findByEmail(req.getCurrentEmail());
				// member 값이 없으면 0 반환
				if (CommonUtils.isEmpty(memberDetail)) {
					return result;
				}
				throwForStatus(memberDetail.getMember_status());

				// 비번일치한지 안한지 검사
				int pwChk = passwordChk(req.getCurrentPassword(), memberDetail);

				if (pwChk == 1) {
					result = memberMapper.changeMemberStatus(memberDetail.getMember_id(),
							MemberStatus.RESTORE.getCode());
					return result;
				}
			} catch (MyBatisSystemException e) {
				throw new RestCustomException(ErrorCode.NO_ACCOUNT);
			}

		}
		return result;
	}


	
	/* 회원체크 */
	@Transactional
	public boolean isMember(String email) {
		boolean result = true;
		ResMemberDetail user = memberMapper.findByEmail(email);
		if (user == null) {
			result = false;
		}
		// 탈퇴한 회원이면 예외 발생
		if (user != null && user.getMember_status().equals(MemberStatus.DELETE.getCode())) {
			throw new CustomException(ErrorCode.WITHDRAWN_MEMBER);
		}
		return result;
	}

	/* 이메일 인증시 회원검사 */
	public void memberChk(String toEmail, String type) {
		if(type.equals("findPw")) {
			ResMemberDetail memberDetail = memberMapper.formMember(toEmail);
			// 소셜로그인시
			if (CommonUtils.isEmpty(memberDetail)) {
				throw new RestCustomException(ErrorCode.NO_ACCOUNT);
			}
			if (memberDetail != null) {
				String status = memberDetail.getMember_status();
				if (status.equals(MemberStatus.DELETE.getCode()) || status.equals(MemberStatus.RESTORE.getCode())) {
					throw new RestCustomException(ErrorCode.INELIGIBLE_REQUEST);
				}
			}
		}
		// 회원가입페이지에서 넘어올때
		if (type.equals("insert") && isMember(toEmail)) {
			throw new RestCustomException(ErrorCode.ID_DUPLICATE);
		}
	}

	/* 비밀번호 변경 */
	public void changePassword(ReqChangePassword req) {
		req.setNewPassword(passwordEncoder.encode(req.getNewPassword()));
		int result = memberMapper.changePassword(req);
		if (result != 1) {
			throw new RestCustomException(ErrorCode.FAIL_CHANGE_PASSWORD);
		}
	}

	/*
	 * 주써잉행=========================================================================
	 * =>
	 */
//	회원 이메일로 시퀀스 찾기
	public Long findByMemberId(String member_email) {
		return memberMapper.findByMemberId(member_email);
	}
	
//	회원정보
	public MemberDTO info(Long member_id, @UserData ResMemberDetail detail) {
		MemberDTO memberDTO = new MemberDTO();
		List<FriendRequestDTO> frReceiveList;
		List<FriendRequestDTO> frSendList;
		
		frReceiveList = friendMapper.receiveRequestList(detail.getMember_id());
		frSendList = friendMapper.sendRequestList(detail.getMember_id());
		
		for(FriendRequestDTO frDTO : frReceiveList) {					// 친구신청 받은 경우
			if (frDTO.getMember_send_id().equals(member_id) && frDTO.getMember_receive_id().equals(detail.getMember_id())) {
				memberDTO = memberMapper.findByMemberSeq(member_id);	// member_id : 받은신청 경로에서 온 회원시퀀스
				memberDTO.setFriend_request_status("send");
				
				return memberDTO;
			}
		}
		
		for (FriendRequestDTO frDTO : frSendList) {						// 친구신청 보낸 경우
			if (frDTO.getMember_send_id().equals(detail.getMember_id()) && frDTO.getMember_receive_id().equals(member_id)) {
				memberDTO = memberMapper.findByMemberSeq(member_id);	// member_id : 보낸신청 경로에서 온 회원시퀀스
				memberDTO.setFriend_request_status("receive");
				
				return memberDTO;
			}
		}
		if (CommonUtils.isEmpty(memberDTO.getFriend_request_status())) {// 커스텀 널 체크
			memberDTO = memberMapper.findByMemberSeq(member_id);
			memberDTO.setFriend_request_status("search");				// member_id : 친구찾기 경로에서 온 회원시퀀스
			
			return memberDTO;
		}else {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
	}
	
//	회원 검색
	public List<MemberDTO> search(String member_email, String keyword, int start, int end){
		if (CommonUtils.isEmpty(member_email)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		
		Long myId = memberMapper.findByMemberId(member_email);
		List<MemberDTO> list = memberMapper.search(myId, keyword, start, end);
		List<MemberDTO> sendIdList = memberMapper.findBySendId(myId, keyword);
		
		if (!sendIdList.isEmpty()) {
			list.removeAll(sendIdList);			// 보낸사람 기준 여러명에게 보낸 만큼 중복되어 나오는 데이터 삭제
		}
		for (MemberDTO memberDTO : list) {		// 리스트에서 신청상태를 표시하기 위해 set
			String status = friendMapper.friendRequestStatus(memberDTO.getMember_id(), myId);
			memberDTO.setFriend_request_status(status);
		}
		return list;
	}
	
//	전체회원 수
	public int searchCount(Long member_id, String keyword) {
		int count = memberMapper.searchCount(member_id, keyword);
		
		return count;
	}
}