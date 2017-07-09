package com.vDigit.rpm.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.stereotype.Component;

@Component
public class Contractors {
	private static final Collection<Contractor> contractors = makePreferredContractors();

	public Collection<Contractor> getContractors() {
		return contractors;
	}

	public Contractor getContractor(String phone) {
		for (Contractor c : contractors) {
			if (c.getPhone().contains(phone) || (phone != null && phone.contains(c.getPhone())))
				return c;
		}
		return null;
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
		contractors.add(Contractor.makeNameAndPhone(c.getName(), c.getPhone(), c.getEmail()));
		return c;
	}

	public Contractor remove(String contractorId) {
		Contractor c;

		for (Iterator<Contractor> it = contractors.iterator(); it.hasNext();) {
			c = it.next();
			if (c.getId().equalsIgnoreCase(contractorId)) {
				it.remove();
				return c;
			}
		}

		return null;
	}
}
