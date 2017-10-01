package com.vDigit.rpm.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JobResponse {
	private Collection<Job> jobs = new ArrayList<Job>();

	private Map<String, List<Job>> data;

	public JobResponse() {

	}

	public Collection<Job> getJobs() {
		return jobs;
	}

	public void setJobs(Collection<Job> jobs) {
		this.jobs = jobs;
	}

	public JobResponse(Collection<Job> jobs) {
		this.jobs = jobs;
	}

	public void setData(Map<String, List<Job>> data) {
		this.data = data;
	}

	public Map<String, List<Job>> getData() {
		return data;
	}

}
