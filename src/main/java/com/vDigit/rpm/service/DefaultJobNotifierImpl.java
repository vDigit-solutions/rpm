package com.vDigit.rpm.service;

import java.text.DateFormat;
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
import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.ContractorPhoneCodeJob;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.dto.PropertyManager;
import com.vDigit.rpm.dto.PropertyManagers;
import com.vDigit.rpm.util.MailNotification;
import com.vDigit.rpm.util.PropertyManagerFactory;
import com.vDigit.rpm.util.TwilioPhoneNotification;

@Component
public class DefaultJobNotifierImpl implements JobNotifier {

	private com.vDigit.rpm.util.PropertyManager PROPERTY_MANAGER = PropertyManagerFactory.getPropertyManager();

	private static final String SUBJECT = "We have a %s work for you";
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
		Contractor c = job.getPotentialNextContractor(contractors.getContractors(job.getType()));
		if (c == null) {
			jobDAO.save(job);
			return;
		}
		Job.ContractorEntry jce = new Job().new ContractorEntry();
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
		String subject = String.format(SUBJECT, c.getType());
		Map<String, String> tokens = createContext(job, c);
		sendSMSIfConfigured(job, c, subject, tokens);
		sendEmail(job, c, subject, tokens);
	}

	protected void sendEmail(Job job, Contractor c, String subject, Map<String, String> tokens) {
		NotificationContext mail = new NotificationContext(null, c.getEmail(), createEmailMessage(tokens, job, c),
				subject);
		mail.setJobId(job.getId());
		mailNotification.send(mail);
	}

	protected void sendSMSIfConfigured(Job job, Contractor c, String subject, Map<String, String> tokens) {
		if ("false".equalsIgnoreCase(PROPERTY_MANAGER.getPhoneNotifications())) {
			return;
		}
		PropertyManager propertyManager = propertyManagers.getPropertyManager(job.getPropertyManagerId());
		tokens.put("manager.phone", propertyManager.getPhone());
		NotificationContext sms = new NotificationContext(null, c.getPhone(), createMessage(tokens, job, c), subject);
		twilioPhoneNotification.send(sms);
	}

	protected Map<String, String> createContext(Job job, Contractor c) {
		Map<String, String> tokens = new HashMap<>();
		tokens.put("name", c.getFirstName());
		tokens.put("type", job.getType());
		tokens.put("propertyName", job.getPropertyName());
		tokens.put("description", job.getDescription());
		tokens.put("location", job.getJobLocation());
		tokens.put("date", format.format(job.getDesiredDateOfBegin()));
		PropertyManager propertyManager = propertyManagers.getPropertyManager(job.getPropertyManagerId());
		tokens.put("manager", propertyManager.getName());
		return tokens;
	}

	private String createEmailMessage(Map<String, String> tokens, Job job, Contractor c) {
		tokens.put("yes", String.format(YES, appUrl, job.getId(), c.getId()));
		tokens.put("no", String.format(NO, appUrl, job.getId(), c.getId()));
		tokens.put("unsubscribe", String.format(UNSUBSCRIBE, appUrl, c.getId()));
		PropertyManager propertyManager = propertyManagers.getPropertyManager(job.getPropertyManagerId());
		tokens.put("manager", propertyManager.getName());
		tokens.put("manager_phone", propertyManager.getPhone());
		return templateMessageReader.read("message_template", tokens, REGEX);
	}

	private String createMessage(Map<String, String> tokens, Job job, Contractor c) {

		ContractorPhoneCodeJob codeMapping = createJobPhoneMapping(job, c);
		ContractorPhoneCodeJob obj = contractorPhoneCodeJobMappingDao.save(codeMapping);
		tokens.put("yes", obj.getYes() + "");
		tokens.put("no", obj.getNo() + "");
		logger.info("ContractorPhoneCodeJob id: {}, jobId: {}, contactorId: {}, Yes: {}, No: {}", obj.getId(),
				obj.getJobId(), obj.getContractorId(), obj.getYes(), obj.getNo());
		return templateMessageReader.read("sms_template", tokens, REGEX);
	}

	private ContractorPhoneCodeJob createJobPhoneMapping(Job job, Contractor c) {
		ContractorPhoneCodeJob mapping = new ContractorPhoneCodeJob(c.getId(), job.getId());
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

	@Override
	public void processManagerConfirmation(Job job, Contractor contractor) {
		PropertyManager pm = propertyManagers.getPropertyManager(job.getPropertyManagerId());
		String pmPhone = pm.getPhone();

		Map<String, String> tokens = new HashMap<>();
		tokens.put("name", pm.getName());
		tokens.put("vendorCompanyName", contractor.getVendorCompanyName());
		tokens.put("type", contractor.getType());
		tokens.put("propertyName", job.getPropertyName());
		tokens.put("date", format.format(job.getDesiredDateOfBegin()));
		tokens.put("firstName", contractor.getFirstName());
		tokens.put("phone", contractor.getPhone());

		String message = templateMessageReader.read("pm_confirmation_template", tokens, REGEX);
		NotificationContext context = new NotificationContext(null, pmPhone, message, null);
		twilioPhoneNotification.send(context);

	}

}
