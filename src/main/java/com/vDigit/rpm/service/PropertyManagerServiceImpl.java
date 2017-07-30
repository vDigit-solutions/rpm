package com.vDigit.rpm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.dto.ScheduleRequest;
import com.vDigit.rpm.util.Util;

@Component
public class PropertyManagerServiceImpl implements PropertyManagerService {

	private static Logger logger = LoggerFactory.getLogger(PropertyManagerServiceImpl.class);
	@Autowired
	private JobDAO jobDAO;

	@Autowired
	private JobNotifier jobNotifier;

	@Override
	public JobResponse createJob(JobRequest jobRequest) {
		Job job = jobRequest.getJob();
		job.setPropertyManagerId(jobRequest.getPropertyManagerId());
		job.setStatus("Schedule");
		Job j = jobDAO.save(job);
		JobResponse jr = new JobResponse();
		jr.getJobs().add(j);
		// new Thread(() -> processJob(j)).start();
		return jr;
	}

	public void processJob(Job job) {
		jobNotifier.processJob(job);
	}

	@Override
	public JobResponse getJobs(JobRequest jobRequest) {
		logger.info(Util.toJSON(jobRequest));
		String pid = jobRequest.getPropertyManagerId();
		String contractorRequestId = jobRequest.getContractorRequestId();
		Collection<Job> jobs = null;
		// Need to implement CoR
		if (pid != null) {
			jobs = jobDAO.findByPropertyManagerId(pid);
		}
		if (contractorRequestId != null) {
			jobs = jobDAO.findByCurrentContractorRequestId(contractorRequestId);
		}
		JobResponse jr = new JobResponse();
		jr.setJobs(jobs);
		return jr;
	}

	@Override
	public JobResponse getJobs() {
		Collection<Job> jobs = null;
		// Need to implement CoR
		jobs = jobDAO.findAll();
		JobResponse jr = new JobResponse();
		jr.setJobs(jobs);
		return jr;
	}

	@Override
	public void updateJob(Job job) {
		jobDAO.save(job);

	}

	@Override
	public void deleteJobs() {
		jobDAO.deleteAll();
	}

	@Override
	public JobResponse scheduleJob(ScheduleRequest scheduleRequest) {
		Iterable<Job> jobs = jobDAO.findAll(scheduleRequest.getJobIds());
		for (Job job : jobs) {
			new Thread(() -> processJob(job)).start();
		}
		Iterable<Job> latest = jobDAO.findAll(scheduleRequest.getJobIds());
		return new JobResponse(StreamSupport.stream(latest.spliterator(), false).collect(Collectors.toList()));
	}

	@Override
	public JobResponse getJobs(String jobId) {
		Job job = jobDAO.findOne(jobId);
		Collection<Job> jobs = new ArrayList<>();
		jobs.add(job);
		return new JobResponse(jobs);
	}

}
