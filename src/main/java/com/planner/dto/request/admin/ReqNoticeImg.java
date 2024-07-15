package com.planner.dto.request.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqNoticeImg {

	private String saveName; 		// 저장 이미지 명
	
	private String originalName; 	// 이미지 원본명
	
	private String extension;		// 확장자명
}
