package util;

import java.util.regex.Pattern;

public class Validator {
	public static boolean isTelephone(String inputStr) {
		String regTelephone = "^1[3|5|7|8][0-9]\\d{8}$";
		return Pattern.matches(regTelephone, inputStr);
	}
	
	public static boolean isPositiveInt(String inputStr) {
		String regPositiveInt = "^\\d+$";
		 return Pattern.matches(regPositiveInt, inputStr);
	}
	
	public static boolean isNonnegativeFloat(String inputStr) {
		String regNonnegativeFloat = "^\\d+(.\\d+)$";
		return Pattern.matches(regNonnegativeFloat, inputStr);
	}
}
