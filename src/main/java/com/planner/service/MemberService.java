package com.planner.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.enums.Gender;
import com.planner.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;


	//	회원가입
	public int memberInsert(MemberDTO memberDTO) {
		memberDTO.setMember_password(passwordEncoder.encode(memberDTO.getMember_password()));

		return memberMapper.memberInsert(memberDTO);
	}
	
	/* 내 정보 */
	public ResMemberDetail memberDetail(String member_email) {
		ResMemberDetail detail =  memberMapper.findByEmail(member_email);
		return detail;
	}
}