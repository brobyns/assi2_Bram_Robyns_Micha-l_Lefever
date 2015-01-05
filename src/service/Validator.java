package service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	private static Pattern regexPattern;
	private static Matcher regMatcher;

	public static boolean isValidEmailAddress(String emailAddress) {
		regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
		regMatcher   = regexPattern.matcher(emailAddress);
		return regMatcher.matches();
	}
	
	public static boolean isAlphabetical(String input){
		regexPattern = Pattern.compile("^[A-Za-z]+$");
		regMatcher = regexPattern.matcher(input);
		return regMatcher.matches();
	}
	
	public static boolean isValidLength(String input, int min, int max){
		int length = input.trim().length();
		return length >= min && length <= max;
	}
	
	public static boolean isValidAmount(String input){
		double amount;
		try{
			amount = Double.parseDouble(input);
		}catch(NumberFormatException e){
			return false;
		}
		return amount > 0 && amount < 1000000;
	}
	
}