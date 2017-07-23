package com.vDigit.rpm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.Job;

@Component
public class JobServiceImpl implements JobService {
	@Autowired
	private JobDAO jobDAO;

	@Override
	public Job getJob(String jobId) {
		return jobDAO.findOne(jobId);
	}
}
