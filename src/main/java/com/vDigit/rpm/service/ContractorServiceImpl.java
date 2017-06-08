package com.vDigit.rpm.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;

public class ContractorServiceImpl implements ContractorService {

	private Contractors contractors = new Contractors();

	@Autowired
	private PropertyManagerService pms;

	@Override
	public void notifyContractor(ContractorRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processContractorResponse(ContractorRequest request) {
		String phone = request.getContractorPhoneNumber();
		String response = request.getContractorResponseForJob();
		String contractorId = getContractorId(phone);
		Job job = getJob(contractorId);
	}

	private String getContractorId(String phone) {
		Contractor c = contractors.getContractor(phone);
		if (c != null)
			return c.getId();
		return null;
	}

	private Job getJob(String contractorId) {
		JobRequest request = new JobRequest();
		request.setPotentialContractorRequestId(contractorId);
		JobResponse jr = pms.getJobs(request);
		return jr.getJobs().iterator().next();
	}

}
