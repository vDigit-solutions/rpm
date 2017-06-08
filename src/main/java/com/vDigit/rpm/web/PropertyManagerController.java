package com.vDigit.rpm.web;

import org.springframework.beans.factory.annotation.Autowired;
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
public class PropertyManagerController {

	@Autowired
	private PropertyManagerService pms;

	@RequestMapping(value = "/job/{propertyManagerId}", method = RequestMethod.POST)
	public @ResponseBody JobResponse createJob(@PathVariable("propertyManagerId") String propertyManagerId,
			@RequestBody JobRequest jobRequest) {
		// System.out.println("createJob");
		jobRequest.getJob().setPropertyManagerId(propertyManagerId);
		JobResponse response = pms.createJob(jobRequest);
		return response;
	}

	@RequestMapping(value = "/job/{propertyManagerId}", method = RequestMethod.GET)
	public @ResponseBody JobResponse getJobs(@PathVariable("propertyManagerId") String propertyManagerId) {
		JobRequest jr = new JobRequest();
		jr.setPropertyManagerId(propertyManagerId);
		return pms.getJobs(jr);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "Test..";
	}
}
