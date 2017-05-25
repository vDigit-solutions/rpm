package com.vDigit.rpm.dto;

public class JobRequest {

	public JobRequest() {
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public String getPropertyManagerId() {
		return propertyManagerId;
	}

	public void setPropertyManagerId(String propertyManagerId) {
		this.propertyManagerId = propertyManagerId;
	}

	private Job job;
	private String propertyManagerId;
}
