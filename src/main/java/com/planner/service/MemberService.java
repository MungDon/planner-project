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
import com.planner.enums.CodeStatus;
import com.planner.enums.MemberRole;
import com.planner.enums.MemberStatus;
import com.planner.exception.CustomException;
import com.planner.exception.ErrorCode;
import com.planner.exception.RestCustomException;
import com.planner.mapper.EmailMapper;
import com.planner.mapper.FriendMapper;
import com.planner.mapper.MemberMapper;
import com.planner.util.CommonUtils;
import com.planner.util.UserData;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final EmailMapper emailMapper;
	private final FriendMapper friendMapper;
	private final PasswordEncoder passwordEncoder;
	private static final boolean MEMBER = true;
	private static final boolean NON_MEMBER = false;
	
	// 회원가입
	@Transactional
	public int memberInsert(MemberDTO memberDTO) {
		String emailAuthStatus = emailMapper.findStatusByToEmail(memberDTO.getMember_email());
		CommonUtils.throwCustomExceptionIf(CommonUtils.isEmpty(emailAuthStatus)||emailAuthStatus.equals(CodeStatus.CODE_UNCHECKED.getStatus()), ErrorCode.FAIL_CODE_AUTHENTICATION);
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
			log.info("여기옴?");
			memberStatusToException(request, response, ErrorCode.WITHDRAWN_MEMBER);
		}
		if (statusCode.equals(MemberStatus.RESTORE.getCode())) {
			memberStatusToException(request, response, ErrorCode.ACCOUNT_RESTORE_PENDING);
		}
	}

	/* 비번체크 */
	public void isPasswordValid(String currnetPw, ResMemberDetail member) {
		CommonUtils.throwRestCustomExceptionIf(CommonUtils.isEmpty(member), ErrorCode.NO_ACCOUNT);
		CommonUtils.throwRestCustomExceptionIf(member.getOauth_id().equals("none") && CommonUtils.isEmpty(currnetPw), ErrorCode.NO_ACCOUNT);
		CommonUtils.throwRestCustomExceptionIf(!passwordEncoder.matches(currnetPw, member.getMember_password()), ErrorCode.NO_ACCOUNT);
	}

	/* 회원 탈퇴 */
	@Transactional
	public void memberDelete(Long member_id) {
		int isTeamMaster = memberMapper.isTeamMaster(member_id);
		CommonUtils.throwRestCustomExceptionIf(isTeamMaster!=0, ErrorCode.GROUP_LEADER_CANNOT_WITHDRAW);
		memberMapper.changeMemberStatus(member_id, MemberStatus.DELETE.getCode());
	}

	/* 회원복구시 회원 상태코드별 반환 예외 */
	private void throwForStatus(String statusCode) {
		String status = MemberStatus.codeToStatus(statusCode);
		switch (status) {
		case "복구신청":
			throw new RestCustomException(ErrorCode.REQUEST_DUPLICATE);
		case "가입":
		case "가입미완료":
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
		// 복구시 입력정보로 회원정보 가져오기
		ResMemberDetail memberDetail = findMemberByLoginType(req.getOauth_type(), req.getCurrentEmail());
		// 회원아니면 예외
		CommonUtils.throwRestCustomExceptionIf(CommonUtils.isEmpty(memberDetail), ErrorCode.NO_ACCOUNT);
		// 비번확인(일반로그인이용할경우)
		isPasswordValid(req.getCurrentPassword(), memberDetail);
		// 회원상태에 따른 예외
		throwForStatus(memberDetail.getMember_status());
		// 성공하면 복구신청상태로변경
		return memberMapper.changeMemberStatus(memberDetail.getMember_id(), MemberStatus.RESTORE.getCode());
	}

	/* 회원정보. */
	@Transactional(readOnly = true)
	private ResMemberDetail findSocialOrFormMember(String email) {
		ResMemberDetail user = memberMapper.socialMember(email);
		if (CommonUtils.isEmpty(user)) {
			user = memberMapper.formMember(email);
		}
		return user;
	}

	/* 회원체크 */
	@Transactional(readOnly = true)
	private void memberValidate(String email, boolean shouldBeMember) {
		ResMemberDetail user = findSocialOrFormMember(email);
		// 회원이 아니여야하는데 회원일때
		CommonUtils.throwRestCustomExceptionIf(!shouldBeMember && !CommonUtils.isEmpty(user), ErrorCode.ID_DUPLICATE);
		// 회원이여야하는데 회원이아닐때
		CommonUtils.throwRestCustomExceptionIf(shouldBeMember && CommonUtils.isEmpty(user),ErrorCode.NO_ACCOUNT);
		// 탈퇴한 회원이면 예외 발생
		CommonUtils.throwRestCustomExceptionIf(!CommonUtils.isEmpty(user) && user.getMember_status().equals(MemberStatus.DELETE.getCode()), ErrorCode.WITHDRAWN_MEMBER);

	}

	/*비번 찾기시 소셜로그인 회원이면 되돌리기*/
	@Transactional(readOnly = true)
	private void isSocialMember(String member_email) {
		ResMemberDetail socialMember = memberMapper.socialMember(member_email);
		ResMemberDetail formMember = memberMapper.formMember(member_email);
		CommonUtils.throwRestCustomExceptionIf(!CommonUtils.isEmpty(socialMember)&&CommonUtils.isEmpty(formMember), ErrorCode.SOCIAL_LOGIN_USER);
	}
	
	/* 이메일 인증시 타입별 회원검사 */
	@Transactional(readOnly = true)
	public void memberChk(String toEmail, String type) {
		switch (type) {
			case "insert" -> memberValidate(toEmail, NON_MEMBER);
			case "findPw" -> {
				isSocialMember(toEmail);
				memberValidate(toEmail, MEMBER);
			}
		}
	}

	/* 비밀번호 변경 */
	@Transactional
	public void changePassword(ReqChangePassword req) {
		req.setNewPassword(passwordEncoder.encode(req.getNewPassword()));
		int result = memberMapper.changePassword(req);
		CommonUtils.throwRestCustomExceptionIf(result != 1, ErrorCode.FAIL_CHANGE_PASSWORD);
	}

	/*
	 * 주써잉행=========================================================================
	 * =>
	 */
//	회원정보
	@Transactional(readOnly = true)
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
	@Transactional(readOnly = true)
	public List<MemberDTO> search(Long member_id, String keyword, int start, int end){
		if (CommonUtils.isEmpty(member_id)) {
			throw new CustomException(ErrorCode.NO_ACCOUNT);
		}
		
		List<MemberDTO> list = memberMapper.search(member_id, keyword, start, end);
		return list;
	}
	
//	전체회원 수
	@Transactional(readOnly = true)
	public int searchCount(Long member_id, String keyword) {
		int count = memberMapper.searchCount(member_id, keyword);
		
		return count;
	}
}