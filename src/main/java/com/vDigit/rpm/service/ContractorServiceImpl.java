package com.vDigit.rpm.service;

import java.text.MessageFormat;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.dto.PropertyManager;
import com.vDigit.rpm.dto.PropertyManagers;
import com.vDigit.rpm.util.PhoneNotification;

@Component
public class ContractorServiceImpl implements ContractorService {

	@Resource(name = "contractors")
	private Contractors contractors;

	@Resource(name = "propertyManagers")
	private PropertyManagers propertyManagers;

	@Autowired
	private PropertyManagerService pms;

	@Autowired
	private PhoneNotification pn;

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
		job.updateContractorResponse(contractorId, response);
		if (response.equalsIgnoreCase("No")) {
			pms.processJob(job);
		} else {
			pms.updateJob(job);
			sendNotificationToPropertyManager(job, contractors.getContractor(phone));
		}
	}

	private void sendNotificationToPropertyManager(Job job, Contractor contractor) {
		PropertyManager pm = propertyManagers.getPropertyManager(job.getPropertyManagerId());
		String pmName = pm.getName();
		String pmPhone = pm.getPhone();
		String message = "Hi {0},\n{1} is interested in doing job [{2}].\nPlease call {1} @ {3} for further information\n";
		String fm = MessageFormat.format(message, pmName, contractor.getLastName(), job.getDescription(),
				contractor.getPhone());
		pn.sendMessage(pmPhone, fm);

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
