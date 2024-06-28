package com.planner.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmailMapper {

	void save(@Param(value = "toEmail")String toEmail,@Param(value = "chkCode")String chkCoded);
}
