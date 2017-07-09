package com.vDigit.rpm.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.vDigit.rpm.dao.ContractorDao;

@Component
public class Contractors {
	private static final Collection<Contractor> contractors = makePreferredContractors();

	@Resource
	private ContractorDao contractorDao;

	public Collection<Contractor> getContractors() {
		return contractorDao.findAll();
	}

	public Contractor getContractor(String phone) {
		/*
		 * for (Contractor c : contractors) { if (c.getPhone().contains(phone)
		 * || (phone != null && phone.contains(c.getPhone()))) return c; }
		 */
		List<Contractor> contracors = contractorDao.findByPhoneLike(phone);
		if (CollectionUtils.isEmpty(contracors)) {
			return null;
		}
		return contracors.iterator().next();
	}

	private static Collection<Contractor> makePreferredContractors() {
		ArrayList<Contractor> contractors = new ArrayList<Contractor>();
		contractors.add(Contractor.makeNameAndPhone("Keith", "3104089637", "keith@rpm.com"));
		contractors.add(Contractor.makeNameAndPhone("Sasan", "2067904659", "Sasan@rpm.com"));
		contractors.add(Contractor.makeNameAndPhone("Ramesh", "4259496967", "Ramesh@rpm.com"));
		contractors.add(Contractor.makeNameAndPhone("Siva", "4252837905", "Siva@rpm.com"));
		// contractors.add(Contractor.makeNameAndPhone("101", "Radhika",
		// "4253066608"));
		return contractors;
	}

	public Contractor creatContractor(Contractor c) {
		Contractor saved = contractorDao.save(c);
		return saved;
	}

	public Contractor remove(String contractorId) {
		Contractor c;

		/*
		 * for (Iterator<Contractor> it = contractors.iterator(); it.hasNext();)
		 * { c = it.next(); if (c.getId().equalsIgnoreCase(contractorId)) {
		 * it.remove(); return c; } }
		 */
		c = contractorDao.findOne(contractorId);
		contractorDao.delete(c);
		return c;
	}
}
