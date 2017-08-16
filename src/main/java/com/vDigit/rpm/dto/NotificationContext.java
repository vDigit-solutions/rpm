package com.vDigit.rpm.dto;

public class NotificationContext {

	private String from;
	private String to;
	private String message;

	private String subject;

	private ContractorPhoneCodeJob contractorPhoneCodeJob;

	public NotificationContext(String from, String to, String message, String subject) {
		this.from = from;
		this.to = to;
		this.message = message;
		this.subject = subject;
	}

	public void setContractorPhoneCodeJob(ContractorPhoneCodeJob contractorPhoneCodeJob) {
		this.contractorPhoneCodeJob = contractorPhoneCodeJob;
	}

	public ContractorPhoneCodeJob getContractorPhoneCodeJob() {
		return contractorPhoneCodeJob;
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
