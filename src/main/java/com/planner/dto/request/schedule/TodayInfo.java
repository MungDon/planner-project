package com.planner.dto.request.schedule;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodayInfo {

	private String currentDate;
	private String today;
}
