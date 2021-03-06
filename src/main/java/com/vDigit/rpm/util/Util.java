package com.vDigit.rpm.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	public static boolean isInternetDOWN() {
		try {
			InetAddress.getByName("www.google.com");
			return false;
		} catch (UnknownHostException uhe) {
			return true;
		}
	}

	// Test
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String toJSON(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static String getServerName() {
		return getUserName() + "-" + getHostName();
	}

	public static String getUserName() {
		return System.getProperty("user.name");
	}

	private static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
			return "NULL HOST";
		}
	}

}
