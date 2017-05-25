package com.vDigit.rpm.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;

@Component
public class PropertyManagerServiceImpl implements PropertyManagerService {

	@Autowired
	private JobDAO jobDAO;

	@Autowired
	private JobNotifier jobNotifier;

	@Override
	public JobResponse createJob(JobRequest jobRequest) {

		Job job = jobRequest.getJob();
		job.setPropertyManagerId(jobRequest.getPropertyManagerId());
		Job j = jobDAO.save(job);
		JobResponse jr = new JobResponse();
		jr.getJobs().add(j);
		new Thread(() -> processJob(j)).start();
		return jr;
	}

	public void processJob(Job job) {
		jobNotifier.processJob(job);
	}

	@Override
	public JobResponse getJobs(JobRequest jobRequest) {
		String pid = jobRequest.getPropertyManagerId();
		Collection<Job> jobs = jobDAO.findByPropertyManagerId(pid);
		JobResponse jr = new JobResponse();
		jr.setJobs(jobs);
		return jr;
	}

}
