package com.vDigit.rpm.util;

public interface PhoneNotification {
	String sendMessage(String phoneNumber, String message);

	String receiveResponse(String phoneNumber);
}
