package com.planner.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.planner.dto.request.member.ReqOAuth2MemberAdd;
import com.planner.dto.response.member.ResOAuth2MemberLogin;

@Mapper
public interface MemberMapper {
	
	/*소셜 회원가입*/
	void createMember(ReqOAuth2MemberAdd req);
	
	/*소셜 회원정보 가져오기*/
	ResOAuth2MemberLogin findByEmail(@Param(value = "email")String email);
}
