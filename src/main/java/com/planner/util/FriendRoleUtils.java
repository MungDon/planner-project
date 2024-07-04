package com.planner.util;

import com.planner.enums.FriendRole;
import com.planner.enums.Masking;

public class FriendRoleUtils {
	public static String getFriendRoleName(String code) {
		return FriendRole.fromString(code);
	}
	
	public static String getMemberMaskingName(String value, Masking masking) {
		return Masking.maskAs(value, masking);
	}
}