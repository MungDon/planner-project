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
		memberValidate(memberDTO.getMember_email(), false);
		memberDTO.setUserDefaults(passwordEncoder);
		return memberMapper.memberInsert(memberDTO);
	}

	/* 일반로그인회원 정보가져오기 */
	@Transactional(readOnly = true)
	public ResMemberDetail formMember(String member_email) {
		return memberMapper.formMember(member_email);
	}

	/* 회원 정보 수정 */
	@Transactional
	public void memberUpdate(ReqMemberUpdate req) {
		memberMapper.memberUpdate(req);
	}

	/* 로그인시 회원상태코드에 따른 예외 */
	private void memberStatusToException(HttpServletRequest request, HttpServletResponse response,
			ErrorCode errorCode) {
		CommonUtils.removeCookiesAndSession(request, response);
		throw new CustomException(errorCode);
	}

	/* 로그인시 회원 상태 코드 체크 */
	public void memberStatusChk(String statusCode, HttpServletRequest request, HttpServletResponse response) {
		if (statusCode.equals(MemberStatus.DELETE.getCode())) {
			memberStatusToException(request, response, ErrorCode.WITHDRAWN_MEMBER);
		}
		if (statusCode.equals(MemberStatus.RESTORE.getCode())) {
			memberStatusToException(request, response, ErrorCode.ACCOUNT_RESTORE_PENDING);
		}
	}

	/* 비번체크 */
	public void isPasswordValid(String currnetPw, ResMemberDetail member) {
		if (CommonUtils.isEmpty(member)) {
			throw new RestCustomException(ErrorCode.NO_ACCOUNT);
		}

		if (member.getOauth_id().equals("none") && !CommonUtils.isEmpty(currnetPw)) {
			throw new RestCustomException(ErrorCode.NO_ACCOUNT);
		}

		if (!passwordEncoder.matches(currnetPw, member.getMember_password())) {
			throw new RestCustomException(ErrorCode.NO_ACCOUNT);
		}
	}

	/* 회원 탈퇴 */
	@Transactional
	public void memberDelete(Long member_id) {
		memberMapper.changeMemberStatus(member_id, MemberStatus.DELETE.getCode());
	}

	/* 회원복구시 회원 상태코드별 반환 예외 */
	private void throwForStatus(String statusCode) {
		String status = MemberStatus.codeToStatus(statusCode);
		switch (status) {
		case "복구신청":
			throw new RestCustomException(ErrorCode.REQUEST_DUPLICATE);
		case "가입":
		case "탈퇴":
			throw new RestCustomException(ErrorCode.INELIGIBLE_REQUEST);
		}
	}

	/* 로그인타입에 따른 객체 반환 */
	@Transactional(readOnly = true)
	public ResMemberDetail findMemberByLoginType(String oauthType, String currentEmail) {
		if (!CommonUtils.isEmpty(oauthType)) {
			return memberMapper.findByEmailAndOAuthType(currentEmail, oauthType);
		}
		return memberMapper.formMember(currentEmail);
	}

	/* 회원 복구 */
	@Transactional
	public int memberRestore(ReqMemberRestore req) {
		ResMemberDetail memberDetail = findMemberByLoginType(req.getOauth_type(), req.getCurrentEmail());
		if (CommonUtils.isEmpty(memberDetail)) {
			throw new RestCustomException(ErrorCode.NO_ACCOUNT);
		}
		isPasswordValid(req.getCurrentPassword(), memberDetail);
		throwForStatus(memberDetail.getMember_status());
		return memberMapper.changeMemberStatus(memberDetail.getMember_id(), MemberStatus.RESTORE.getCode());
	}

	/* 회원정보. */
	@Transactional(readOnly = true)
	private ResMemberDetail findSocialOrFormMember(String email) {
		ResMemberDetail user = memberMapper.socialMember(email);
		if (user == null) {
			user = memberMapper.formMember(email);
		}
		return user;
	}

	/* 회원체크 */
	@Transactional(readOnly = true)
	public void memberValidate(String email, boolean shouldBeMember) {
		ResMemberDetail user = findSocialOrFormMember(email);

		// 회원이아니여야하는데 회원정보가 있을때
		if (!shouldBeMember && !CommonUtils.isEmpty(user)) {
			throw new CustomException(ErrorCode.ID_DUPLICATE);
		}

		// 회원이여야하는데 회원정보가 없을때
		if (shouldBeMember && CommonUtils.isEmpty(user)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}

		// 탈퇴한 회원이면 예외 발생
		if (user.getMember_status().equals(MemberStatus.DELETE.getCode())) {
			throw new CustomException(ErrorCode.WITHDRAWN_MEMBER);
		}
	}

	/*비번 찾기시 소셜로그인 회원이면 되돌리기*/
	@Transactional(readOnly = true)
	public void isSocialMember(String member_email) {
		ResMemberDetail user = memberMapper.socialMember(member_email);
		if(!CommonUtils.isEmpty(user)) {
			throw new RestCustomException(ErrorCode.SOCIAL_LOGIN_USER);
		}
	}
	
	/* 이메일 인증시 타입별 회원검사 */
	@Transactional(readOnly = true)
	public void memberChk(String toEmail, String type) {
		switch (type) {
			case "insert" -> memberValidate(toEmail, false);
			case "findPw" -> {
				isSocialMember(toEmail);
				memberValidate(toEmail, true);
			}
		}
	}

	/* 비밀번호 변경 */
	@Transactional
	public void changePassword(ReqChangePassword req) {
		req.setNewPassword(passwordEncoder.encode(req.getNewPassword()));
		int result = memberMapper.changePassword(req);
		CommonUtils.RestResultSuccessful(result, ErrorCode.FAIL_CHANGE_PASSWORD);
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

		for (FriendRequestDTO frDTO : frReceiveList) { // 친구신청 받은 경우
			if (frDTO.getMember_send_id().equals(member_id)
					&& frDTO.getMember_receive_id().equals(detail.getMember_id())) {
				memberDTO = memberMapper.findByMemberSeq(member_id); // member_id : 받은신청 경로에서 온 회원시퀀스
				memberDTO.setFriend_request_status("send");

				return memberDTO;
			}
		}

		for (FriendRequestDTO frDTO : frSendList) { // 친구신청 보낸 경우
			if (frDTO.getMember_send_id().equals(detail.getMember_id())
					&& frDTO.getMember_receive_id().equals(member_id)) {
				memberDTO = memberMapper.findByMemberSeq(member_id); // member_id : 보낸신청 경로에서 온 회원시퀀스
				memberDTO.setFriend_request_status("receive");

				return memberDTO;
			}
		}
		if (CommonUtils.isEmpty(memberDTO.getFriend_request_status())) {// 커스텀 널 체크
			memberDTO = memberMapper.findByMemberSeq(member_id);
			memberDTO.setFriend_request_status("search"); // member_id : 친구찾기 경로에서 온 회원시퀀스

			return memberDTO;
		} else {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
	}

//	회원 검색
	public List<MemberDTO> search(String member_email, String keyword, int start, int end) {
		if (CommonUtils.isEmpty(member_email)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}

		Long myId = memberMapper.findByMemberId(member_email);
		List<MemberDTO> list = memberMapper.search(myId, keyword, start, end);
		List<MemberDTO> sendIdList = memberMapper.findBySendId(myId, keyword);

		if (!sendIdList.isEmpty()) {
			list.removeAll(sendIdList); // 보낸사람 기준 여러명에게 보낸 만큼 중복되어 나오는 데이터 삭제
		}
		for (MemberDTO memberDTO : list) { // 리스트에서 신청상태를 표시하기 위해 set
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