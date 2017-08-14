package com.vDigit.rpm.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.vDigit.rpm.dto.ContractorPhoneCodeJob;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.service.ContractorService;
import com.vDigit.rpm.service.JobService;
import com.vDigit.rpm.util.TwilioPhoneNotification;

@RestController
@RequestMapping("/api/sms")
@CrossOrigin(origins = "*")
public class TwilioPhoneController {

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
		return phone.replace("+1", "");
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
		if (!NumberUtils.isNumber(body)) {
			wrongResponseCode(f, body);
			return;
		}
		int code = Integer.parseInt(body);
		List<ContractorPhoneCodeJob> mapping = contractorPhoneCodeJobMappingDao.findByYesOrNo(code, code);

		if (CollectionUtils.isEmpty(mapping)) {
			wrongResponseCode(f, body);
			return;
		}
		ContractorPhoneCodeJob phoneJobMapping = mapping.iterator().next();
		contractorPhoneCodeJobMappingDao.delete(phoneJobMapping);
		ContractorRequest cr = new ContractorRequest();
		cr.setContractor(contractors.getContractorById(phoneJobMapping.getContractorId()));
		cr.setJob(jobService.getJob(phoneJobMapping.getJobId()));
		cr.setContractorResponseForJob(phoneJobMapping.getYes() == code ? "yes" : "no");
		processContractorResponse(cr);
	}

	private void wrongResponseCode(String f, String body) {
		String x;
		x = "Thank you for your response[" + body + "]. However, I don't understand " + body
				+ ". Can you please respond with either Yes or No";
		NotificationContext ctx = new NotificationContext(null, f, x, null);
		pn.send(ctx);
	}

	private void processContractorResponse(ContractorRequest request) {
		new Thread(() -> contractorService.processContractorResponse(request)).start();
	}
}
