package com.vDigit.rpm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.vDigit.rpm.dao.ContractorPhoneCodeJobMappingDao;
import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.ContractorPhoneCodeJob;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.dto.PropertyManagers;
import com.vDigit.rpm.dto.ScheduleRequest;
import com.vDigit.rpm.util.CreatedDateComparator;
import com.vDigit.rpm.util.Util;

@Component
public class PropertyManagerServiceImpl implements PropertyManagerService {

	private static Logger logger = LoggerFactory.getLogger(PropertyManagerServiceImpl.class);
	@Autowired
	private JobDAO jobDAO;

	@Autowired
	private JobNotifier jobNotifier;

	@Resource(name = "contractorPhoneCodeJobMappingDao")
	private ContractorPhoneCodeJobMappingDao contractorPhoneCodeJobMappingDao;

	@Resource(name = "propertyManagers")
	private PropertyManagers propertyManagers;

	private ExecutorService executor = Executors.newCachedThreadPool();

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
		executor.submit(new JobProcessor(job));
	}

	class JobProcessor implements Callable<Void> {

		private Job job;

		public JobProcessor(Job job) {
			this.job = job;
		}

		@Override
		public Void call() throws Exception {
			jobNotifier.processJob(job);
			return null;
		}

	}

	class ManagerConfirmationProcessor implements Callable<Void> {

		private final Job job;
		private final Contractor contractor;

		public ManagerConfirmationProcessor(Job job, Contractor contractor) {
			this.job = job;
			this.contractor = contractor;
		}

		@Override
		public Void call() throws Exception {
			jobNotifier.processManagerConfirmation(job, contractor);
			return null;
		}

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
		List<Job> j = new ArrayList<>(jobs);
		Collections.sort(j, new CreatedDateComparator());
		jr.setJobs(j);
		return jr;
	}

	@Override
	public void updateJob(Job job, Contractor contractor) {
		jobDAO.save(job);
		executor.submit(new ManagerConfirmationProcessor(job, contractor));
	}

	@Override
	public void deleteJobs() {
		jobDAO.deleteAll();
		contractorPhoneCodeJobMappingDao.deleteAll();
	}

	@Override
	public JobResponse scheduleJob(ScheduleRequest scheduleRequest) {
		Iterable<Job> jobs = jobDAO.findAll(scheduleRequest.getJobIds());
		for (Job job : jobs) {
			executor.submit(new JobProcessor(job));
		}
		Iterable<Job> latest = jobDAO.findAll(scheduleRequest.getJobIds());
		List<Job> j = new ArrayList<>();
		latest.forEach(j::add);
		Collections.sort(j, new CreatedDateComparator());
		return new JobResponse(j);
	}

	@Override
	public JobResponse getJobs(String jobId) {
		Job job = jobDAO.findOne(jobId);
		Collection<Job> jobs = new ArrayList<>();
		jobs.add(job);
		return new JobResponse(jobs);
	}

	@Override
	public void delete(String jobId) {
		jobDAO.delete(jobId);
		deleteJobMapping(jobId);
	}

	@Override
	public void deleteJobMapping(String jobId) {
		List<ContractorPhoneCodeJob> jobMappings = contractorPhoneCodeJobMappingDao.findByJobId(jobId);
		delete(jobMappings);
	}

	private void delete(List<ContractorPhoneCodeJob> jobMappings) {
		if (CollectionUtils.isEmpty(jobMappings)) {
			return;
		}
		for (ContractorPhoneCodeJob contractorPhoneCodeJob : jobMappings) {
			contractorPhoneCodeJobMappingDao.delete(contractorPhoneCodeJob);
		}
	}

	@Override
	public void deleteJobMapping(String jobId, String contractorId) {
		List<ContractorPhoneCodeJob> jobMappings = contractorPhoneCodeJobMappingDao.findByJobIdAndContractorId(jobId,
				contractorId);
		delete(jobMappings);
	}

}
