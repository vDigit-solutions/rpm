package com.vDigit.rpm.service;

import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.dto.ScheduleRequest;

public interface PropertyManagerService {
	JobResponse createJob(JobRequest jobRequest);

	JobResponse getJobs(JobRequest jobRequest);

	void processJob(Job job);

	void updateJob(Job job);

	void deleteJobs();

	JobResponse getJobs();

	JobResponse scheduleJob(ScheduleRequest scheduleRequest);

	JobResponse getJobs(String jobId);

	void delete(String jobId);

	void deleteJobMapping(String jobId);

	void deleteJobMapping(String jobId, String contractorId);
}
