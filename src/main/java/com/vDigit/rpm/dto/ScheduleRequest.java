package com.vDigit.rpm.dto;

import java.util.List;

public class ScheduleRequest {
	private List<String> jobIds;

	public void setJobIds(List<String> jobIds) {
		this.jobIds = jobIds;
	}

	public List<String> getJobIds() {
		return jobIds;
	}
}
