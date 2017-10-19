package com.vDigit.rpm.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.Job.ContractorEntry;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.dto.ScheduleRequest;
import com.vDigit.rpm.persistence.model.User;
import com.vDigit.rpm.service.ContractorService;
import com.vDigit.rpm.service.JobService;
import com.vDigit.rpm.service.PropertyManagerService;
import com.vDigit.rpm.util.Util;

@RestController
@RequestMapping("/api/pm")
@CrossOrigin(origins = "*")
public class JobsController {
	private static final String DECLINED = "declined";

	private static final String ACCEPTED = "accepted";

	private static final String YES = "yes";

	private static final Logger logger = LoggerFactory.getLogger(JobsController.class);

	private final DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

	private static final String RESENDING_JOB_CONFIRMATION = "Thanks for responding. However, you have %s the job";

	private static final String YES_RESPONSE = "Thank you for your reply. You are scheduled to %s: %s %s %s";

	private static final String NO_RESPONSE = "Thanks for responding.  We will see you next time.";

	private static final String WRONG_JOB_CODE = "Thank you for your response[%s]. However, this job is no longer avaliable or has been booked.";

	@Autowired
	private PropertyManagerService pms;

	@Resource(name = "contractors")
	private Contractors contractors;

	@Resource(name = "jobServiceImpl")
	private JobService jobService;

	@Autowired
	private ContractorService contractorService;

	@RequestMapping(value = "/jobs", method = RequestMethod.POST)
	public @ResponseBody JobResponse createJob(@RequestBody JobRequest jobRequest, Authentication authentication) {
		jobRequest.setPropertyManagerId(getUser(authentication).getId());
		return pms.createJob(jobRequest);
	}

	@RequestMapping(value = "/schedule/jobs", method = RequestMethod.PUT)
	public @ResponseBody JobResponse scheduleJob(@RequestBody ScheduleRequest scheduleRequest) {
		logger.info("Schedule Job -> " + Util.toJSON(scheduleRequest));
		return pms.scheduleJob(scheduleRequest);
	}

	@RequestMapping(value = "/reschedule/jobs", method = RequestMethod.PUT)
	public @ResponseBody JobResponse rescheduleJob(@RequestBody ScheduleRequest scheduleRequest) {
		logger.info("Reschedule Job -> " + Util.toJSON(scheduleRequest));
		return pms.scheduleJob(scheduleRequest);
	}

	@RequestMapping(value = "/job/{jobId}", method = RequestMethod.GET)
	public @ResponseBody JobResponse getJob(@PathVariable String jobId) {
		return pms.getJobs(jobId);
	}

	@RequestMapping(value = "/job/{jobId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String jobId) {
		pms.delete(jobId);
	}

	@RequestMapping(value = "/jobs", method = RequestMethod.DELETE)
	public void deleteJobs(Authentication authentication) {
		pms.deleteJobs(getUser(authentication).getId());
	}

	@RequestMapping(value = "/jobs", method = RequestMethod.GET)
	public @ResponseBody JobResponse getJobs(Authentication authentication) {
		JobRequest jr = new JobRequest();
		jr.setPropertyManagerId(getUser(authentication).getId());
		return pms.getJobs(jr);
	}

	private User getUser(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return user;
	}

	@RequestMapping(value = "/job/{jobId}/{contractorId}/{acceptance}", method = RequestMethod.GET)
	public @ResponseBody String getJobs(@PathVariable("jobId") String jobId,
			@PathVariable("contractorId") String contractorId, @PathVariable("acceptance") String acceptance) {
		Job job = jobService.getJob(jobId);
		if (job == null) {
			return String.format(WRONG_JOB_CODE, acceptance);
		}
		Contractor contractor = contractors.getContractorById(contractorId);
		ContractorEntry entry = job.getContractorEntry(contractorId);
		if (entry != null) {
			String resp = entry.getResponse();
			if (StringUtils.isNotBlank(resp)) {
				return String.format(RESENDING_JOB_CONFIRMATION, resp.equalsIgnoreCase(YES) ? ACCEPTED : DECLINED);
			}
		}
		ContractorRequest cr = new ContractorRequest();
		cr.setJob(job);
		cr.setContractor(contractor);
		cr.setContractorResponseForJob(acceptance);
		logger.info("JobId: {}, ContractorId: {}, Response: {}", jobId, contractorId, acceptance);
		processContractorResponse(cr);
		if (acceptance.equalsIgnoreCase(YES)) {
			return String.format(YES_RESPONSE, job.getType(), job.getPropertyName(),
					format.format(job.getDesiredDateOfBegin()), contractor.getVendorCompanyName());
		}
		return NO_RESPONSE;
	}

	private void processContractorResponse(ContractorRequest request) {
		contractorService.processContractorResponse(request);
	}

}
