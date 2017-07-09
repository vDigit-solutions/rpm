package com.vDigit.rpm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.service.PropertyManagerService;

@RestController
@RequestMapping("/api/pm")
@CrossOrigin(origins = "*")
public class PropertyManagerController {

	@Autowired
	private PropertyManagerService pms;

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
}
