package com.example.hhplus.common.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {

	private DateUtil() {}

	public static String format(Instant time, String pattern) {
		return DateTimeFormatter.ofPattern(pattern)
				.withZone(ZoneId.systemDefault())
				.format(time);
	}
}
