package com.planner.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.request.member.ReqMemberUpdate;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.MemberStatus;
import com.planner.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;


	//	회원가입
	@Transactional
	public int memberInsert(MemberDTO memberDTO) {
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
	public int passwordChk(String currnetPw,String member_email){
		int result = 0;
		ResMemberDetail member = memberMapper.findByEmail(member_email);
		if(passwordEncoder.matches(currnetPw, member.getMember_password())) {
			return result = 1;
		}
		return result;
	}
	
	/*회원 탈퇴*/
	public void memberDelete(String member_email) {
		memberMapper.memberDelete(member_email, MemberStatus.DELETE.getCode());
	}
}