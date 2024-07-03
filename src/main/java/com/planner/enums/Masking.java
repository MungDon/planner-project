package com.planner.enums;

public enum Masking {
	Name,
	Phone;

	public static String maskAs(String value, Masking masking) {
		String maskingValue = value;
		
		switch (masking) {
			case Name:
				if (value.length() == 2) {
					maskingValue = "*" + value.substring(1);
				}else if (value.length() == 3) {
					maskingValue = value.substring(0, 1) + "*" + value.substring(2);
				}else if (value.length() >= 4) {
					maskingValue = value.substring(0,1) + "**" + value.substring(3);
				}
				break;
				
			case Phone:
				if (value.length() >= 8 && value.length() <= 10) {
					maskingValue = (value.length() == 8) ? 
					value.substring(0,4) + "****" : 
					value.substring(0,4) + "****" + value.substring(8);
				}else if (value.length() >= 11) {
					maskingValue = value.substring(0,5) + "****" + value.substring(9);
				}
				break;
		}
		return maskingValue;
	}
}