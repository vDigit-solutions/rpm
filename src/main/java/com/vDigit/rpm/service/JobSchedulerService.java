package com.vDigit.rpm.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.Job.ContractorEntry;

@Component
public class JobSchedulerService {

	private static final Logger logger = LoggerFactory.getLogger(JobSchedulerService.class);

	@Resource(name = "contractorServiceImpl")
	private ContractorService contractorService;

	@Resource(name = "contractors")
	private Contractors contractors;

	@Autowired
	private JobDAO jobDAO;

	@Scheduled(cron = "0 0 */1 * * MON-FRI")
	// @Scheduled(cron = "0 0 */1 * * ?")
	public void schedule() {
		logger.info("Scheduler started...");
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.DATE, -1);
		cal.add(Calendar.HOUR, -1);
		List<Job> jobs = jobDAO.findByStatusAndStatusDateLessThanEqual("In Progress", cal.getTime());
		if (CollectionUtils.isEmpty(jobs)) {
			logger.info("Scheduler no jobs found...");
			return;
		}
		for (Job job : jobs) {
			Map<String, ContractorEntry> entries = job.getContractorEntries();
			String theLastKey = new ArrayList<>(entries.keySet()).get(entries.size() - 1);
			ContractorEntry contractorEntry = entries.get(theLastKey);
			ContractorRequest cr = new ContractorRequest();
			cr.setJob(job);
			cr.setContractor(contractors.getContractorById(contractorEntry.getId()));
			cr.setContractorResponseForJob("no");
			contractorService.processContractorResponse(cr);
		}
		logger.info("Scheduler finished...");
	}
}
