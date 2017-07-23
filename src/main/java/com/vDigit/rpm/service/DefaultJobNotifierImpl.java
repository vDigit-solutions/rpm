package com.vDigit.rpm.service;

import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.ContractWork;
import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.util.MailNotification;
import com.vDigit.rpm.util.TwilioPhoneNotification;

@Component
public class DefaultJobNotifierImpl implements JobNotifier {

	@Resource(name = "contractors")
	private Contractors contractors;

	@Autowired
	private JobDAO jobDAO;

	@Autowired
	private TwilioPhoneNotification twilioPhoneNotification;

	@Resource(name = "mailNotification")
	private MailNotification mailNotification;

	@Value("${app.url:http://localhost:8080}")
	private String appUrl;

	private static final String YES = "%s/api/pm/job/%s/%s/yes";
	private static final String NO = "%s/api/pm/job/%s/%s/no";

	@Override
	public void processJob(Job job) {
		ContractWork cw = job.getContractWork();
		if (cw != null) {
			return;
		}
		Contractor c = job.getPotentialNextContractor(contractors.getContractors(job.getType()));
		if (c == null) {
			return;
		}
		Job.ContractorEntry jce = new Job.ContractorEntry();
		jce.setId(c.getId());
		jce.setNotificationSentDate(new Date());
		job.addContractorEntry(jce);

		// We need to do a CoR or observer pattern here..
		jobDAO.save(job);
		notifyContractor(job, c);
	}

	private void notifyContractor(Job job, Contractor c) {

		NotificationContext sms = new NotificationContext(null, c.getPhone(), createMessage(job, c));
		twilioPhoneNotification.send(sms);

		NotificationContext mail = new NotificationContext(null, c.getEmail(), createEmailMessage(job, c));
		mailNotification.send(mail);
	}

	private String createEmailMessage(Job job, Contractor c) {
		StringBuffer sb = new StringBuffer();
		try {
			org.springframework.core.io.Resource resource = new ClassPathResource("message_template");
			StringWriter writer = new StringWriter();
			IOUtils.copy(resource.getInputStream(), writer, "utf-8");
			String orderXml = writer.toString();
			Map<String, String> tokens = new HashMap<>();
			tokens.put("name", c.getLastName());
			tokens.put("description", job.getDescription());
			tokens.put("location", job.getJobLocation());
			tokens.put("date", job.getDesiredDateOfBegin().toString());
			tokens.put("yes", String.format(YES, appUrl, job.getId(), c.getId()));
			tokens.put("no", String.format(NO, appUrl, job.getId(), c.getId()));

			// Create pattern of the format "%(name|date)%"
			String patternString = "-(" + StringUtils.join(tokens.keySet(), "|") + ")-";
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(orderXml);

			while (matcher.find()) {
				matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
			}
			matcher.appendTail(sb);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sb.toString();
	}

	private String createMessage(Job job, Contractor c) {
		String message = "Hi {0},\nWe have a contract work.\nWork Description:\n{1}\n\nLocation of work : \n{2}\n\nExpected Date of Start : \n{3}\n\nPlease respond with YES (if you are interested) and NO (if you are not interested).\n";
		return MessageFormat.format(message, c.getLastName(), job.getDescription(), job.getJobLocation(),
				job.getDesiredDateOfBegin().toString());
	}

}
