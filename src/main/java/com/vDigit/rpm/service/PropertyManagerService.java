package com.vDigit.rpm.service;

import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;

public interface PropertyManagerService {
	JobResponse createJob(JobRequest jobRequest);

	JobResponse getJobs(JobRequest jobRequest);
}
