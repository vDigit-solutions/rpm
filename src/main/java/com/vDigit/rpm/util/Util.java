package com.vDigit.rpm.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String toJSON(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
