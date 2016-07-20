package com.commander4j.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JRegularExpression {

	public String findMatch(String line, String pattern, int occur) {
		String result = "";

		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(line);

		if (m.find()) {
			result = m.group(occur);
		} 

		return result;
	}
}
