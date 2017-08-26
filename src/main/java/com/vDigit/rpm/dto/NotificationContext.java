package com.vDigit.rpm.dto;

public class NotificationContext {

	private String from;
	private String to;
	private String message;
	private String subject;

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	private String jobId;

	public NotificationContext(String from, String to, String message, String subject) {
		this.from = from;
		this.to = to;
		this.message = message;
		this.subject = subject;
	}

	public NotificationContext() {

	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
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
