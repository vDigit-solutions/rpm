package com.vDigit.rpm.dto;

public class NotificationContext {

	private String from;
	private String to;
	private String message;

	private String subject;

	public NotificationContext(String from, String to, String message, String subject) {
		this.from = from;
		this.to = to;
		this.message = message;
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
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
