package com.planner.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDTO {
	private Long notice_id;
	private String notice_title;
	private String notice_content;
}
