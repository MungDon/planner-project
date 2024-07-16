package com.planner.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.planner.dto.request.admin.NoticeDTO;
import com.planner.dto.request.admin.ReqNoticeImg;
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
		 int result =  noticeMapper.noticeInsert(noticeDTO);
		 insertHotelSid(noticeDTO.getNotice_id());
		return result;
	}
	/*이미지 테이블에 공지사항 시퀀스 삽입*/
	private void insertHotelSid(Long notice_id) {
		// 저장된 소개글 내용중 이미지 파일명만 가져옴
		List<String> imgFileNames = extractImgFileName(notice_id);
		for(String imgFileName : imgFileNames) {
			noticeMapper.insertNoticeId(imgFileName, notice_id);
		}
	}
	
	private List<String> extractImgFileName(Long notice_id) {
	    String content = noticeMapper.findContentBynoticeId(notice_id);
	    List<String> imgFileNames = new ArrayList<>();

	    // 이미지 태그에서 src 속성 값에서 파일명 추출
	    String imgPattern = "<img[^>]+src\\s*=\\s*\"([^\">]+)\"";
	    Pattern pattern = Pattern.compile(imgPattern);
	    Matcher matcher = pattern.matcher(content);

	    while (matcher.find()) {
	        String src = matcher.group(1); // img 태그의 src 속성 값
	        // 파일명 추출: 마지막 '/' 이후의 문자열
	        String fileName = src.substring(src.lastIndexOf('/') + 1);
	        imgFileNames.add(fileName);
	    }

	    return imgFileNames;
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
		List<String> imgNames = extractImgFileName(notice_id);
		for(String imgName : imgNames) {
			noticeMapper.deleteImg(imgName);
		}
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
	
	/*잉여 이미지 데이터 삭제(스케줄러)*/
	@Transactional
	public void deleteImagesWithoutNoticeId() {
		List<String> imgNames = noticeMapper.ImagesWithoutNoticeId();
		for(String imgName : imgNames) {
			deleteImg(imgName);
		}
		noticeMapper.deleteImagesWithoutNoticeId();
	}
}	
