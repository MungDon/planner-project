package com.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planner.dto.request.admin.NoticeDTO;
import com.planner.mapper.NoticeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {
	private final NoticeMapper noticeMapper;
	
	@Transactional
	public int noticeInsert(NoticeDTO noticeDTO) {
		return noticeMapper.noticeInsert(noticeDTO);
	}
	
	@Transactional(readOnly = true)
	public List<NoticeDTO> noticeSelect( int pageNum, int pageSize){
		int start = (pageNum - 1)*pageSize + 1;
		int end = pageSize * pageNum;
		return noticeMapper.noticeSelect(start, end);
	}
	
	@Transactional(readOnly = true)
	public int noticeAllSelect() {
		return noticeMapper.noticeAllSelect();
	}
	
	@Transactional(readOnly = true)
	public NoticeDTO noticeContent(Long notice_id) {
		return noticeMapper.noticeContent(notice_id);
	}
	
	@Transactional
	public int noticeUpdate(NoticeDTO dto) {
		return noticeMapper.noticeUpdate(dto);
	}
	
	@Transactional
	public int noticeDelete(Long notice_id) {
		return noticeMapper.noticeDelete(notice_id);
	}
}	
