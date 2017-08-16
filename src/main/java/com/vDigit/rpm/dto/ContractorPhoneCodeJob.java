package com.vDigit.rpm.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ContractorPhoneCodeJob {
	@Id
	private String id;

	private String contractorId;
	private String jobId;
	private Integer yes;
	private Integer no;

	public ContractorPhoneCodeJob(String contractorId, String jobId) {
		this.contractorId = contractorId;
		this.jobId = jobId;
	}

	public String getId() {
		return id;
	}

	public String getContractorId() {
		return contractorId;
	}

	public String getJobId() {
		return jobId;
	}

	public Integer getYes() {
		return yes;
	}

	public void setYes(Integer yes) {
		this.yes = yes;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

}
