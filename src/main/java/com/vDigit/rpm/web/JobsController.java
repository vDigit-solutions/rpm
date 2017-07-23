package com.vDigit.rpm.web;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.service.ContractorService;
import com.vDigit.rpm.service.JobService;
import com.vDigit.rpm.service.PropertyManagerService;

@RestController
@RequestMapping("/api/pm")
@CrossOrigin(origins = "*")
public class JobsController {

	@Autowired
	private PropertyManagerService pms;

	@Resource(name = "contractors")
	private Contractors contractors;

	@Resource(name = "jobServiceImpl")
	private JobService jobService;

	@Autowired
	private ContractorService contractorService;

	@RequestMapping(value = "/jobs", method = RequestMethod.POST)
	public @ResponseBody JobResponse createJob(@RequestBody JobRequest jobRequest) {
		return pms.createJob(jobRequest);
	}

	@RequestMapping(value = "/jobs", method = RequestMethod.GET)
	public @ResponseBody JobResponse getJobs() {
		return pms.getJobs();
	}

	@RequestMapping(value = "/jobs", method = RequestMethod.DELETE)
	public void deleteJobs() {
		pms.deleteJobs();
	}

	@RequestMapping(value = "/job/{propertyManagerId}", method = RequestMethod.GET)
	public @ResponseBody JobResponse getJobs(@PathVariable("propertyManagerId") String propertyManagerId) {
		JobRequest jr = new JobRequest();
		jr.setPropertyManagerId(propertyManagerId);
		return pms.getJobs(jr);
	}

	@RequestMapping(value = "/job/{jobId}/{contractorId}/{acceptance}", method = RequestMethod.GET)
	public @ResponseBody String getJobs(@PathVariable("jobId") String jobId,
			@PathVariable("contractorId") String contractorId, @PathVariable("acceptance") String acceptance) {
		Job job = jobService.getJob(jobId);
		Contractor contractor = contractors.getContractorById(contractorId);
		ContractorRequest cr = new ContractorRequest();
		cr.setJob(job);
		cr.setContractorPhoneNumber(contractor.getPhone());
		cr.setContractorResponseForJob(acceptance);

		processContractorResponse(cr);

		return "thankyou";
	}

	private void processContractorResponse(ContractorRequest request) {
		new Thread(() -> contractorService.processContractorResponse(request)).start();
	}

}
