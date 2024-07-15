package com.planner.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmailMapper {

	/*인증상태 가져오기*/
	String findStatusByToEmail(@Param(value = "toEmail")String toEmail);
	
	/*인증 정보 저장*/
	int saveAuthCode(@Param(value = "toEmail")String toEmail,@Param(value = "authCode")String authCode,@Param(value = "codeStatus")String codeStatus);
	
	/*인증 정보 검증*/
	int authCodeChk(@Param(value = "toEmail")String toEmail,@Param(value = "authCode")String authCode);

	/*인증 기록이 있는지 없는지*/
	int isExists(@Param(value = "toEmail")String toEmail);
	
	/*인증 기록삭제*/
	void deleteEmailAuthCode(@Param(value = "toEmail")String toEmail);
	
	/*잉여 데이터 전부 삭제*/
	void removeAll();
	
	/*인증확인 변경*/
	void updateCodeChk(@Param(value = "toEmail")String toEmail,@Param(value = "codeStatus")String codeStatus);
}
