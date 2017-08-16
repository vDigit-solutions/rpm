package com.vDigit.rpm.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.dto.PropertyManager;
import com.vDigit.rpm.dto.PropertyManagers;
import com.vDigit.rpm.util.MailNotification;
import com.vDigit.rpm.util.TwilioPhoneNotification;

@Component
public class JobConfirmationNotifier {
	private static final String REGEX = "-(%s)-";
	@Resource(name = "templateMessageReader")
	private TemplateMessageReader templateMessageReader;

	private final DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

	@Autowired
	private TwilioPhoneNotification twilioPhoneNotification;

	@Resource(name = "mailNotification")
	private MailNotification mailNotification;

	@Resource(name = "propertyManagers")
	private PropertyManagers propertyManagers;

	public void process(String template, Job job, Contractor contractor) {
		Map<String, String> tokens = new HashMap<>();
		tokens.put("name", contractor.getFirstName());
		tokens.put("type", job.getType());
		tokens.put("today", format.format(new Date()));
		tokens.put("propertyName", job.getPropertyName());
		tokens.put("address", job.getJobLocation());
		tokens.put("description", job.getDescription());
		tokens.put("date", format.format(job.getDesiredDateOfBegin()));
		PropertyManager propertyManager = propertyManagers.getPropertyManager(job.getPropertyManagerId());
		tokens.put("propertyManager", propertyManager.getName());
		String msg = templateMessageReader.read(template, tokens, REGEX);
		notifyContractor(contractor, msg, "Wrok Order Confirmation");
	}

	private void notifyContractor(Contractor c, String message, String subject) {
		String phoneMsg = message.replaceAll("<div>", "").replaceAll("</div>", "");
		NotificationContext sms = new NotificationContext(null, c.getPhone(), phoneMsg, subject);
		twilioPhoneNotification.send(sms);

		NotificationContext mail = new NotificationContext(null, c.getEmail(), message, subject);
		mailNotification.send(mail);
	}

}