package com.planner.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

	MALE("M", "남성"),
	FEMALE("W", "여성");
	
	private final String code;
	private final String name;
	
	// DB에는 Code, 즉 W나 M으로 등록되어있어 사용자에게 코드를 보여줄 수 없기에
	// 등록된코드를 name 으로변환하여 가저오는코드 
	public static String findNameByCode(String code) {
		for(Gender gender : Gender.values()) {
			if(gender.getCode().equals(code)) {
				return gender.getName();
			}
		}
		return "성별이 등록되지않음";
	}
}
