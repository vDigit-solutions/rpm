package com.vDigit.rpm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;

@RestController
@RequestMapping("/api/sms")
public class TwilioPhoneController {

	private ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/job", method = RequestMethod.POST)
	public String receiveMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String f = request.getParameter("From");
		String body = request.getParameter("Body");
		String mid = request.getParameter("MessageSid");
		String msid = request.getParameter("MessagingServiceId");
		String x = f + " -> " + body;
		System.out.println(x + " -> " + mid+" -> "+msid);
		x = "Hello -> I recieved \"" + body + "\" from " + f + ". Thank you for your message.";
		Message message = new Message.Builder().body(new Body(x)).build();

		MessagingResponse twiml = new MessagingResponse.Builder().message(message).build();

		response.setContentType("application/xml");

		/*
		 * try { response.getWriter().print(twiml.toXml()); } catch
		 * (TwiMLException e) { e.printStackTrace(); }
		 */
		// return twiml.toXml();
		return x;
	}
}
