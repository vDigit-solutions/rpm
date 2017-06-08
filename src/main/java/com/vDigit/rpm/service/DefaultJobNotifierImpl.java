package com.vDigit.rpm.service;

import java.text.MessageFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.ContractWork;
import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.util.PhoneNotification;

@Component
public class DefaultJobNotifierImpl implements JobNotifier {

	private Contractors contractors = new Contractors();

	@Autowired
	private JobDAO jobDAO;

	@Autowired
	private PhoneNotification phoneNotification;

	@Override
	public void processJob(Job job) {
		ContractWork cw = job.getContractWork();
		if (cw != null) {
			return;
		}
		Contractor c = job.getPotentialNextContractor(contractors.getContractors());
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
		PhoneNotification pn = phoneNotification;
		pn.sendMessage(c.getPhone(), createMessage(job, c));
	}

	private String createMessage(Job job, Contractor c) {
		String message = "Hi {0},\nWe have a contract work {1}\n.Location of work : {2}\n.Expected Date of Start : {3}\n.Please respond with YES (if you are interested) and NO (if you are not interested)\n";
		return MessageFormat.format(message, c.getName(), job.getDescription(), job.getJobLocation(),
				job.getDesiredDateOfBegin().toString());
	}

}
