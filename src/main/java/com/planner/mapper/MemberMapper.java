package com.planner.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.planner.dto.request.member.MemberDTO;
import com.planner.dto.request.member.ReqMemberUpdate;
import com.planner.dto.request.member.ReqOAuth2MemberAdd;
import com.planner.dto.request.member.ReqOAuth2Signup;
import com.planner.dto.response.member.ResMemberDetail;
import com.planner.dto.response.member.ResOAuth2MemberLogin;

@Mapper
public interface MemberMapper {
	
	/*소셜 회원가입*/
	void createMember(ReqOAuth2MemberAdd req);

	//	회원가입
	int memberInsert(MemberDTO memberDTO);
	
	/*소셜 회원정보 가져오기*/
	ResOAuth2MemberLogin findByOAuthID(@Param(value = "oauthId")String oauthId);
	
	/*소셜로그인에서 제공받지 못한 유저정보 저장*/
	void fetchAdditionalUserInfo(ReqOAuth2Signup req);
	
	//	회원 이메일로 객체 가져오기
	MemberDTO findByUser(@Param(value = "member_email")String member_email);

	/*내 정보*/
	ResMemberDetail findByEmail(@Param(value = "member_email") String member_email);
	
	/*회원 수정*/
	void memberUpdate(ReqMemberUpdate req);
}
