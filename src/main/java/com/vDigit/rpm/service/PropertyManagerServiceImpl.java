package com.vDigit.rpm.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.util.Util;

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
		System.out.println(Util.toJSON(jobRequest));
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
	public void updateJob(Job job) {
		jobDAO.save(job);
		
	}

}