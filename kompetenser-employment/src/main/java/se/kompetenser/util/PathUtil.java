package se.kompetenser.util;

import java.util.regex.Pattern;

public class PathUtil {

	private static final Pattern LONG_PATTERN = Pattern.compile("(-)?\\d+");

	public static Long parseLongParam(String param) {
		return LONG_PATTERN.matcher(param).matches() ? Long.valueOf(param) : null;
	}

	private PathUtil() {
		// Utility
	}
	
	public static Integer[] getIntArray(String arr) {
		final String[] items = arr.split(",");
		final Integer[] results = new Integer[items.length];
		for (int i = 0; i < items.length; i++) {
			results[i] = Integer.parseInt(items[i]);
		}
		return results;
		
	}

}