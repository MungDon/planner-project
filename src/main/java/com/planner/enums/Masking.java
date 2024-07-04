package com.planner.enums;

public enum Masking {
	NAME,
	PHONE;

	public static String maskAs(String value, Masking masking) {
		String maskingValue = value;
		
		switch (masking) {
			case NAME:
				if (value.length() == 2) {
					maskingValue = value.substring(0, 1) + "*";
				}else if (value.length() == 3) {
					maskingValue = value.substring(0, 1) + "*" + value.substring(2);
				}else if (value.length() >= 4) {
					maskingValue = value.substring(0, 1) + "**" + value.substring(3);
				}
				break;
				
			case PHONE:
				if (value.length() >= 8 && value.length() <= 10) {
					maskingValue = (value.length() == 8) ? 
					value.substring(0,4) + "****" : 
					value.substring(0,4) + "****" + value.substring(8);
				}else if (value.length() >= 11) {
					maskingValue = value.substring(0,3) + "****" + value.substring(7);
				}
				break;
		}
		return maskingValue;
	}
}