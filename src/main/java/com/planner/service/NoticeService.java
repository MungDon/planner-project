package com.planner.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.planner.dto.request.admin.NoticeDTO;
import com.planner.dto.request.notice.ReqNoticeImg;
import com.planner.exception.ErrorCode;
import com.planner.mapper.NoticeMapper;
import com.planner.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {
	
	@Value("${file.Upimg}")
	private String path;
	
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
	
	/*이미지 업로드*/
	@Transactional
	public String imgUpload(MultipartFile multipartFile) throws IllegalStateException, IOException {
		String originalName = multipartFile.getOriginalFilename();
		String uuid = String.valueOf(UUID.randomUUID());
		String extension = originalName.substring(originalName.lastIndexOf("."));
		String saveName = uuid+extension;
		File file = new File(path+saveName);
		
		if(!file.exists()) {
			file.mkdir();
		}
		multipartFile.transferTo(file);
		ReqNoticeImg noticeImg = ReqNoticeImg.builder()
				.originalName(originalName)
				.extension(extension)
				.saveName(saveName)
				.build();
		noticeMapper.saveImg(noticeImg);
		return saveName;
	}
	
	/*이미지 삭제*/
	@Transactional
	public void deleteImg(String imgName) {
		File file = new File(path+imgName);
		
		if(file.exists()) {
			file.delete();
		}
		
		int result = noticeMapper.deleteImg(imgName);
		CommonUtils.throwRestCustomExceptionIf(result != 1, ErrorCode.DB_DELETE_FAILED);
	}
}	
