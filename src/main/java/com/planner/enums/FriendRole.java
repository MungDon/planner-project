package com.planner.enums;

import lombok.Getter;

@Getter
public enum FriendRole {

	REQUEST("S", "요청중"),
	SUCCESS("F", "친구");
	
	private String code;
	private String name;
	
	FriendRole(String code, String name){
		this.code = code;
		this.name = name;
	}
	
	public static String fromString(String code) {
		for (FriendRole role : FriendRole.values()) {
			if (role.getCode().equals(code)) {
				return role.getName();
			}
		}
		return null;
	}
}
