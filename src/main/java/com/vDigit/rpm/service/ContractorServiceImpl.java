package com.vDigit.rpm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.JobRequest;
import com.vDigit.rpm.dto.JobResponse;
import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.dto.PropertyManager;
import com.vDigit.rpm.dto.PropertyManagers;
import com.vDigit.rpm.util.TwilioPhoneNotification;

@Component
public class ContractorServiceImpl implements ContractorService {

	private static final String REGEX = "-(%s)-";

	@Resource(name = "contractors")
	private Contractors contractors;

	@Resource(name = "propertyManagers")
	private PropertyManagers propertyManagers;

	@Autowired
	private PropertyManagerService pms;

	@Resource(name = "twilioPhoneNotification")
	private TwilioPhoneNotification pn;

	@Resource(name = "templateMessageReader")
	private TemplateMessageReader templateMessageReader;

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
			pms.updateJob(job);
			sendNotificationToPropertyManager(job, contractor);
		}
		pms.deleteJobMapping(job.getId(), contractor.getId());
		sendConfirmationToContractor(confirmationTemplate, job, contractor);
	}

	private void sendConfirmationToContractor(String confirmationTemplate, Job job, Contractor contractor) {
		jobConfirmationNotifier.process(confirmationTemplate, job, contractor);
	}

	private void sendNotificationToPropertyManager(Job job, Contractor contractor) {
		PropertyManager pm = propertyManagers.getPropertyManager(job.getPropertyManagerId());
		String pmPhone = pm.getPhone();
		/*
		 * String message =
		 * "Hi {0},\n{1} is interested in doing job [{2}].\nPlease call {1} @ {3} for further information\n"
		 * ; String fm = MessageFormat.format(message, pmName,
		 * contractor.getFirstName(), job.getDescription(),
		 * contractor.getPhone());
		 */
		Map<String, String> tokens = new HashMap<>();
		tokens.put("name", pm.getName());
		tokens.put("vendorCompanyName", contractor.getVendorCompanyName());
		tokens.put("propertyname", job.getPropertyName());
		tokens.put("date", job.getDesiredDateOfBegin().toString());
		tokens.put("firstName", contractor.getFirstName());
		tokens.put("phone", contractor.getPhone());

		String message = templateMessageReader.read("pm_confirmation_template", tokens, REGEX);
		NotificationContext context = new NotificationContext(null, pmPhone, message, null);
		pn.send(context);

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
