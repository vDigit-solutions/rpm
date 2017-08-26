package com.vDigit.rpm.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vDigit.rpm.dao.ContractorPhoneCodeJobMappingDao;
import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.ContractorPhoneCodeJob;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.Job.ContractorEntry;
import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.service.ContractorService;
import com.vDigit.rpm.service.JobService;
import com.vDigit.rpm.util.TwilioPhoneNotification;

@RestController
@RequestMapping("/api/sms")
@CrossOrigin(origins = "*")
public class TwilioPhoneController {

	private static final String _1 = "+1";

	private static final String DECLINED = "declined";

	private static final String ACCEPTED = "accepted";

	private static final String NO = "no";

	private static final String YES = "yes";

	private static final String RESENDING_JOB_CONFIRMATION = "Thank you for your response. However, you have already %s the job.";

	private static final String WRONG_JOB_CODE = "Thank you for your response[%s]. However, this job is no longer avaliable or has been booked.";

	// Test
	private static Logger logger = LoggerFactory.getLogger(TwilioPhoneController.class);

	@Autowired
	private ContractorService contractorService;

	@Resource(name = "contractors")
	private Contractors contractors;

	@Resource(name = "twilioPhoneNotification")
	private TwilioPhoneNotification pn;

	@Resource(name = "jobServiceImpl")
	private JobService jobService;

	@Resource(name = "contractorPhoneCodeJobMappingDao")
	private ContractorPhoneCodeJobMappingDao contractorPhoneCodeJobMappingDao;

	private String cleanPhoneNumber(String phone) {
		return phone.replace(_1, "");
	}

	@RequestMapping(value = "/job", method = RequestMethod.POST)
	public void receiveMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String f = request.getParameter("From");
		f = cleanPhoneNumber(f);
		String body = request.getParameter("Body");
		// String mid = request.getParameter("MessageSid");
		// String msid = request.getParameter("MessagingServiceId");
		String x = f + " -> " + body;
		logger.info("[TwilioPhoneController]: " + x);
		response.setContentType("application/xml");
		if (StringUtils.isBlank(body)) {
			return;
		}
		String responseCode = body.trim();
		if (!NumberUtils.isNumber(responseCode)) {
			wrongResponseCode(f, responseCode);
			return;
		}
		int code = Integer.parseInt(responseCode);
		List<ContractorPhoneCodeJob> mapping = contractorPhoneCodeJobMappingDao.findByYesOrNo(code, code);

		if (CollectionUtils.isEmpty(mapping)) {
			wrongResponseCode(f, responseCode);
			return;
		}
		ContractorPhoneCodeJob phoneJobMapping = mapping.iterator().next();
		contractorPhoneCodeJobMappingDao.delete(phoneJobMapping);
		Job job = jobService.getJob(phoneJobMapping.getJobId());
		logger.info("JobId:: " + phoneJobMapping.getJobId());

		if (job == null) {
			noJobFound(f, responseCode);
		}

		ContractorEntry entry = job.getContractorEntry(phoneJobMapping.getContractorId());
		String resp = entry.getResponse();

		if (StringUtils.isNotBlank(resp)) {
			resendingJobConfirmation(f, resp.equalsIgnoreCase(YES) ? ACCEPTED : DECLINED);
			return;
		}

		Contractor contractor = contractors.getContractorById(phoneJobMapping.getContractorId());
		logger.info("ContractorId:: " + phoneJobMapping.getContractorId());

		ContractorRequest cr = new ContractorRequest();
		cr.setJob(job);
		cr.setContractor(contractor);
		String respsoneCode = phoneJobMapping.getYes() == code ? YES : NO;
		logger.info("Response :" + respsoneCode);
		cr.setContractorResponseForJob(respsoneCode);
		processContractorResponse(cr);
	}

	private void wrongResponseCode(String f, String body) {
		String x = String.format(WRONG_JOB_CODE, body);
		NotificationContext ctx = new NotificationContext(null, f, x, null);
		pn.send(ctx);
	}

	private void noJobFound(String f, String body) {
		String x = String.format(WRONG_JOB_CODE, body);
		NotificationContext ctx = new NotificationContext(null, f, x, null);
		pn.send(ctx);
	}

	private void resendingJobConfirmation(String f, String body) {
		NotificationContext ctx = new NotificationContext(null, f, String.format(RESENDING_JOB_CONFIRMATION, body),
				null);
		pn.send(ctx);
	}

	private void processContractorResponse(ContractorRequest request) {
		contractorService.processContractorResponse(request);
	}
}
