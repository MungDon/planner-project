package com.planner.service;

import java.security.Principal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.request.member.ReqMemberUpdate;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberStatus;
import com.planner.mapper.MemberMapper;
import com.planner.oauth.service.OAuth2UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;


	//	회원가입
	@Transactional
	public int memberInsert(MemberDTO memberDTO)  {
		ResMemberDetail detail =  memberMapper.findByEmail(memberDTO.getMember_email());
		memberDTO.setMember_password(passwordEncoder.encode(memberDTO.getMember_password()));
		return memberMapper.memberInsert(memberDTO);
	}
	
	/* 내 정보 */
	@Transactional(readOnly = true)
	public ResMemberDetail memberDetail(String member_email) {
		ResMemberDetail detail =  memberMapper.findByEmail(member_email);
		return detail;
	}
	
	/*회원 정보 수정*/
	@Transactional
	public void memberUpdate(ReqMemberUpdate req) {
		memberMapper.memberUpdate(req);
	}
	
	/*비번체크*/
	@Transactional(readOnly = true)
	public int passwordChk(String currnetPw,String member_email){
		int result = 0;
		ResMemberDetail member = memberMapper.findByEmail(member_email);
		if(member != null && !member.getOauth_id().equals("none")) {
			return result = 1;
		}
		if(member != null && passwordEncoder.matches(currnetPw, member.getMember_password())) {
			return result = 1;
		}
		return result;
	}
	
	/*회원 탈퇴*/
	@Transactional
	public void memberDelete(String member_email) {
		memberMapper.changeMemberStatus(member_email, MemberStatus.DELETE.getCode());
	}
	
	/*회원 복구*/
	@Transactional
	public int memberRestore(String currentEmail, String currentPassword) {
		int result = passwordChk( currentPassword, currentEmail);
		if(result == 1) {
			memberMapper.changeMemberStatus(currentEmail, MemberStatus.RESTORE.getCode());
		}
		return result;
	}
	
	/*회원탈퇴여부체크*/
	@Transactional(readOnly = true)
	public boolean isMember(OAuth2UserPrincipal oAuth2UserPrincipal,Principal principal) {
		boolean result = true;
		if(principal.getName().equals("none")) {
			ResMemberDetail member = memberMapper.findByOAuthID(oAuth2UserPrincipal.getOAuthId());
			result = statusChk(member.getMember_status());
		}else {
			ResMemberDetail user = memberMapper.findByEmail(principal.getName());
			result = statusChk(user.getMember_status());
		}
		return result;
	}
	
	/*상태체크*/
	private boolean statusChk(String member_status) {
		if(member_status.equals(MemberStatus.DELETE.getCode())) {
			return false;
		}
		return true;
	}
}