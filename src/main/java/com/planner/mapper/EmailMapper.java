package com.planner.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmailMapper {

	/*인증 정보 저장*/
	int saveAuthCode(@Param(value = "toEmail")String toEmail,@Param(value = "authCode")String authCode);
	
	/*인증 정보 검증*/
	int authCodeChk(@Param(value = "toEmail")String toEmail,@Param(value = "authCode")String authCode);
}
