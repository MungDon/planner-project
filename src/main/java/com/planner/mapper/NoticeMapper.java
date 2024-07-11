package com.planner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.planner.dto.request.admin.NoticeDTO;

@Mapper
public interface NoticeMapper {
	public int noticeInsert(NoticeDTO noticeDTO);
	
	public List<NoticeDTO> noticeSelect(@Param("start") int start,@Param("end") int end);
	
	public int noticeAllSelect();
	
	public NoticeDTO noticeContent(Long notice_id);
	
	public int noticeUpdate(NoticeDTO dto);
	
	public int noticeDelete(Long notice_id);
}
