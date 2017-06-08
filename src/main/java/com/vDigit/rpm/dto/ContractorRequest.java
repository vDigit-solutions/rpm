package com.vDigit.rpm.dto;

public class ContractorRequest {
	private Contractor contractor;
	private Job job;
	private ContractWork contractWork;
	private Contractor nextContractor;

	private String contractorPhoneNumber;
	private String contractorResponseForJob;

	public String getContractorPhoneNumber() {
		return contractorPhoneNumber;
	}

	public void setContractorPhoneNumber(String contractorPhoneNumber) {
		this.contractorPhoneNumber = contractorPhoneNumber;
	}

	public String getContractorResponseForJob() {
		return contractorResponseForJob;
	}

	public void setContractorResponseForJob(String contractorResponseForJob) {
		this.contractorResponseForJob = contractorResponseForJob;
	}
}
