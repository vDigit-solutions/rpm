package com.vDigit.rpm.dto;

public class ContractWork {
	private Contractor contractor;
	private String message;
	private boolean response;

	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}

	public Contractor getContractor() {
		return contractor;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}
}
