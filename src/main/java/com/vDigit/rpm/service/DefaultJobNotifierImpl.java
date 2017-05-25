package com.vDigit.rpm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dao.JobDAO;
import com.vDigit.rpm.dto.ContractWork;
import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.Job;
import com.vDigit.rpm.util.Util;

@Component
public class DefaultJobNotifierImpl implements JobNotifier {

	private Collection<Contractor> preferredContractors = makePreferredContractors();

	@Autowired
	private JobDAO jobDAO;

	@Override
	public void processJob(Job job) {
		ContractWork cw = job.getContractWork();
		if (cw != null) {
			return;
		}
		Contractor c = job.getPotentialNextContracter(preferredContractors);
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
		System.out.println("Notifiying Contractor -> " + Util.toJSON(c));
	}

	private Collection<Contractor> makePreferredContractors() {
		ArrayList<Contractor> contractors = new ArrayList<Contractor>();
		contractors.add(Contractor.makeNameAndPhone("100", "Sasan", "2067904659"));
		contractors.add(Contractor.makeNameAndPhone("101", "Siva", "4252837905"));
		contractors.add(Contractor.makeNameAndPhone("102", "Ramesh", "4259496967"));
		contractors.add(Contractor.makeNameAndPhone("103", "Keith", "3104089637"));
		return contractors;
	}

}
