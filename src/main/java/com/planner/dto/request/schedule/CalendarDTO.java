package com.planner.dto.request.schedule;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarDTO {
	private Long team_id;
	private Long member_id;
	private LocalDate start_date;
	private LocalDate end_date;
}
