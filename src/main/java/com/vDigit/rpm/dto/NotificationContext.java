package com.vDigit.rpm.dto;

public class NotificationContext {

	private String from;
	private String to;
	private String message;

	public NotificationContext(String from, String to, String message) {
		this.from = from;
		this.to = to;
		this.message = message;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getMessage() {
		return message;
	}

}
