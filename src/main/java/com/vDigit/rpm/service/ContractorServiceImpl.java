package com.vDigit.rpm.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.dto.PropertyManagers;

@Component
public class ContractorServiceImpl implements ContractorService {

	@Resource(name = "contractors")
	private Contractors contractors;

	@Resource(name = "propertyManagers")
	private PropertyManagers propertyManagers;

	@Autowired
	private PropertyManagerService pms;

	@Resource(name = "jobConfirmationNotifier")
	private JobConfirmationNotifier jobConfirmationNotifier;

	@Override
	public void notifyContractor(ContractorRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processContractorResponse(ContractorRequest request) {
		String confirmationTemplate = null;
		String phone = request.getContractorPhoneNumber();
		String response = request.getContractorResponseForJob();
		Contractor contractor = getContractorId(request.getContractor(), phone);
		Job job = getJob(request.getJob(), contractor.getId());
		job.updateContractorResponse(contractor.getId(), response);

		if (response.equalsIgnoreCase("No")) {
			confirmationTemplate = "contractor_declained_template";
			pms.processJob(job);
		} else if (response.equalsIgnoreCase("yes")) {
			confirmationTemplate = "contractor_accepted_template";
			job.setStatusDate(new Date());
			job.setStatus("Booked");
			pms.updateJob(job, contractor);
		}
		pms.deleteJobMapping(job.getId(), contractor.getId());
		sendConfirmationToContractor(confirmationTemplate, job, contractor);
	}

	private void sendConfirmationToContractor(String confirmationTemplate, Job job, Contractor contractor) {
		jobConfirmationNotifier.process(confirmationTemplate, job, contractor);
	}

	private Contractor getContractorId(Contractor contractor, String phone) {
		if (contractor != null) {
			return contractor;
		}
		Contractor c = contractors.getContractor(phone);
		if (c != null)
			return c;
		return null;
	}

	private Job getJob(Job job, String contractorId) {
		if (job != null) {
			return job;
		}
		JobRequest request = new JobRequest();
		request.setPotentialContractorRequestId(contractorId);
		JobResponse jr = pms.getJobs(request);
		return jr.getJobs().iterator().next();
	}

}
