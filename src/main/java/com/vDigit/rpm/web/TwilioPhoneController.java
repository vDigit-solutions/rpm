package com.vDigit.rpm.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.service.ContractorService;
import com.vDigit.rpm.util.TwilioPhoneNotification;

@RestController
@RequestMapping("/api/sms")
@CrossOrigin(origins = "*")
public class TwilioPhoneController {

	// Test
	private static Logger logger = LoggerFactory.getLogger(TwilioPhoneController.class);

	@Autowired
	private ContractorService contractorService;

	@Resource(name = "twilioPhoneNotification")
	private TwilioPhoneNotification pn;

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

		if (!(body.equalsIgnoreCase("YES") || body.equalsIgnoreCase("NO"))) {
			x = "Thank you for your response[" + body + "]. However, I don't understand " + body
					+ ". Can you please respond with either Yes or No";
			NotificationContext ctx = new NotificationContext(null, f, x);
			pn.send(ctx);
			return;
		}
		x = "Hello -> I received \"" + body + "\" from " + f + ". Thank you for your message.";
		Message message = new Message.Builder().body(new Body(x)).build();

		// MessagingResponse twiml = new
		// MessagingResponse.Builder().message(message).build();

		ContractorRequest cr = new ContractorRequest();
		cr.setContractorPhoneNumber(f);
		cr.setContractorResponseForJob(body);

		processContractorResponse(cr);

		/*
		 * try { response.getWriter().print(twiml.toXml()); } catch
		 * (TwiMLException e) { e.printStackTrace(); }
		 */
		// return twiml.toXml();

	}

	private void processContractorResponse(ContractorRequest request) {
		new Thread(() -> contractorService.processContractorResponse(request)).start();
	}
}
