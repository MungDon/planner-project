package com.planner.dto.request.schedule;

import lombok.Getter;

@Getter
public class CalendarPrintDTO {
	private Long schedule_id;
	private String schedule_title;
	private String schedule_start;
	private String schedule_end;
}
