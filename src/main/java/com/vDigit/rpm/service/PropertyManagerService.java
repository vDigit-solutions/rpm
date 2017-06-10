package com.vDigit.rpm.service;

import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;

public interface PropertyManagerService {
	JobResponse createJob(JobRequest jobRequest);

	JobResponse getJobs(JobRequest jobRequest);

	void processJob(Job job);

	void updateJob(Job job);
}