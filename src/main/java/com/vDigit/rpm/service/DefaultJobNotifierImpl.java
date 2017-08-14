package com.vDigit.rpm.service;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.vDigit.rpm.dao.ContractorPhoneCodeJobMappingDao;
import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.ContractWork;
import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.ContractorPhoneCodeJob;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.dto.PropertyManager;
import com.vDigit.rpm.dto.PropertyManagers;
import com.vDigit.rpm.util.MailNotification;
import com.vDigit.rpm.util.TwilioPhoneNotification;

@Component
public class DefaultJobNotifierImpl implements JobNotifier {

	private static final String YES = "%s/api/pm/job/%s/%s/yes";
	private static final String NO = "%s/api/pm/job/%s/%s/no";

	private static final String REGEX = "-(%s)-";

	private final DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

	private static final Logger logger = LoggerFactory.getLogger(DefaultJobNotifierImpl.class);

	private Random random = new Random();

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

	@Resource(name = "templateMessageReader")
	private TemplateMessageReader templateMessageReader;

	@Resource(name = "propertyManagers")
	private PropertyManagers propertyManagers;

	private int minimum = 1000;
	private int maximum = 10000;

	@Resource(name = "contractorPhoneCodeJobMappingDao")
	private ContractorPhoneCodeJobMappingDao contractorPhoneCodeJobMappingDao;

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
		job.setStatus("In Progress");
		job.setStatusDate(new Date());
		// We need to do a CoR or observer pattern here..
		jobDAO.save(job);
		notifyContractor(job, c);
	}

	private void notifyContractor(Job job, Contractor c) {
		String subject = "We have a work order for you";
		NotificationContext sms = new NotificationContext(null, c.getPhone(), createMessage(job, c), subject);
		twilioPhoneNotification.send(sms);

		NotificationContext mail = new NotificationContext(null, c.getEmail(), createEmailMessage(job, c), subject);
		mailNotification.send(mail);
	}

	private String createEmailMessage(Job job, Contractor c) {
		Map<String, String> tokens = new HashMap<>();
		tokens.put("name", c.getLastName());
		tokens.put("type", job.getType());
		tokens.put("propertyName", job.getPropertyName());
		tokens.put("description", job.getDescription());
		tokens.put("location", job.getJobLocation());
		tokens.put("date", format.format(job.getDesiredDateOfBegin()));
		tokens.put("yes", String.format(YES, appUrl, job.getId(), c.getId()));
		tokens.put("no", String.format(NO, appUrl, job.getId(), c.getId()));
		PropertyManager propertyManager = propertyManagers.getPropertyManager(job.getPropertyManagerId());
		tokens.put("manager", propertyManager.getName());
		return templateMessageReader.read("message_template", tokens, REGEX);
	}

	private String createMessage(Job job, Contractor c) {
		ContractorPhoneCodeJob codeMapping = createJobPhoneMapping(job, c);
		ContractorPhoneCodeJob obj = contractorPhoneCodeJobMappingDao.save(codeMapping);
		logger.info("ContractorPhoneCodeJob id: {0}, jobId: {1}, contactorId: {2}, Yes: {3}, No: {4}", obj.getId(),
				obj.getJobId(), obj.getContractorId(), obj.getYes(), obj.getNo());
		String message = "Hi {0},\nWe have a contract work.\nWork Description:\n{1}\n\nLocation of work : \n{2}\n\nExpected Date of Start : \n{3}\n\nPlease respond with "
				+ codeMapping.getYes() + " (if you are interested) and " + codeMapping.getNo()
				+ " (if you are not interested).\n";
		return MessageFormat.format(message, c.getFirstName(), job.getDescription(), job.getJobLocation(),
				job.getDesiredDateOfBegin().toString());
	}

	private ContractorPhoneCodeJob createJobPhoneMapping(Job job, Contractor c) {
		ContractorPhoneCodeJob mapping = new ContractorPhoneCodeJob(job.getId(), c.getId());
		List<Integer> vals = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Integer code = generateCode();
			vals.add(code);
		}
		mapping.setYes(vals.get(0));
		mapping.setNo(vals.get(1));
		return mapping;
	}

	private Integer generateCode() {
		Integer code = phoneJobCodeGenerator();
		List<ContractorPhoneCodeJob> records = contractorPhoneCodeJobMappingDao.findByYesOrNo(code, code);
		if (CollectionUtils.isEmpty(records)) {
			return code;
		}
		return generateCode();
	}

	private Integer phoneJobCodeGenerator() {
		int range = maximum - minimum + 1;
		return random.nextInt(range) + minimum;
	}

}
