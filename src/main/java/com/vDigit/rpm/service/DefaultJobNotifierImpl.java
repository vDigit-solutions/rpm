package com.vDigit.rpm.service;

import java.text.MessageFormat;
import java.util.Date;

import javax.annotation.Resource;

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

	@Resource(name = "contractors")
	private Contractors contractors;

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
		PhoneNotification pn = getPhoneNotification();
		pn.sendMessage(c.getPhone(), createMessage(job, c));
	}

	public PhoneNotification getPhoneNotification() {
		// return getDummyPhoneNotification();
		return phoneNotification;
	}

	public PhoneNotification getDummyPhoneNotification() {
		return new PhoneNotification() {

			@Override
			public String sendMessage(String phoneNumber, String message) {
				System.out.println("[Phone,Message] -> " + "[" + phoneNumber + "," + message + "]");
				return message;
			}

			@Override
			public String receiveResponse(String phoneNumber) {
				// TODO Auto-generated method stub
				return null;
			}

		};

	}

	private String createMessage(Job job, Contractor c) {
		String message = "Hi {0},\nWe have a contract work.\nWork Description:\n{1}\n\nLocation of work : \n{2}\n\nExpected Date of Start : \n{3}\n\nPlease respond with YES (if you are interested) and NO (if you are not interested).\n";
		return MessageFormat.format(message, c.getLastName(), job.getDescription(), job.getJobLocation(),
				job.getDesiredDateOfBegin().toString());
	}

}
