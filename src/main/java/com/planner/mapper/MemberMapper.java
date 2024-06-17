package com.planner.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.planner.dto.request.member.ReqOAuth2MemberAdd;
import com.planner.dto.request.member.ReqOAuth2Signup;
import com.planner.dto.response.member.ResOAuth2MemberLogin;

@Mapper
public interface MemberMapper {
	
	/*소셜 회원가입*/
	void createMember(ReqOAuth2MemberAdd req);
	
	/*소셜 회원정보 가져오기*/
	ResOAuth2MemberLogin findByEmail(@Param(value = "email")String email);
	
	/*소셜로그인에서 제공받지 못한 유저정보 저장*/
	void fetchAdditionalUserInfo(ReqOAuth2Signup req);
}
