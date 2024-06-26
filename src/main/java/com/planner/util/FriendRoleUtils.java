package com.planner.util;

import com.planner.enums.FriendRole;

public class FriendRoleUtils {
	public static String getFriendRoleName(String code) {
		return FriendRole.fromString(code);
	}
}