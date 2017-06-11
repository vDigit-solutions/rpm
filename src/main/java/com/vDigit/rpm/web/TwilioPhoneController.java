package com.vDigit.rpm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.vDigit.rpm.dto.ContractorRequest;
import com.vDigit.rpm.service.ContractorService;
import com.vDigit.rpm.util.PhoneNotification;

@RestController
@RequestMapping("/api/sms")
public class TwilioPhoneController {

	@Autowired
	private ContractorService contractorService;

	@Autowired
	private PhoneNotification pn;

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
		System.out.println(x);
		response.setContentType("application/xml");

		if (!(body.equalsIgnoreCase("YES") || body.equalsIgnoreCase("NO"))) {
			x = "Thank you for your response[" + body + "]. However, I don't understand " + body
					+ ". Can you please respond with either Yes or No";
			pn.sendMessage(f, x);
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
