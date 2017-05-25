package com.vDigit.rpm.dto;

import java.util.ArrayList;
import java.util.Collection;

public class JobResponse {
	public Collection<Job> getJobs() {
		return jobs;
	}

	public void setJobs(Collection<Job> jobs) {
		this.jobs = jobs;
	}

	private Collection<Job> jobs = new ArrayList<Job>();
}
