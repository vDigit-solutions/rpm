package com.vDigit.rpm.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Contractors {
	private Collection<Contractor> contractors = makePreferredContractors();

	public Collection<Contractor> getContractors() {
		return contractors;
	}

	public Contractor getContractor(String phone) {
		for (Contractor c : contractors) {
			if (c.getPhone().equals(phone))
				return c;
		}
		return null;
	}

	private Collection<Contractor> makePreferredContractors() {
		ArrayList<Contractor> contractors = new ArrayList<Contractor>();
		contractors.add(Contractor.makeNameAndPhone("104", "Keith", "3104089637", "keith@rpm.com"));
		contractors.add(Contractor.makeNameAndPhone("102", "Sasan", "2067904659", "Sasan@rpm.com"));
		contractors.add(Contractor.makeNameAndPhone("103", "Ramesh", "4259496967", "Ramesh@rpm.com"));
		contractors.add(Contractor.makeNameAndPhone("100", "Siva", "4252837905", "Siva@rpm.com"));
//		contractors.add(Contractor.makeNameAndPhone("101", "Radhika", "4253066608"));
		return contractors;
	}

	public Contractor creatContractor(Contractor c) {
		contractors.add(c);
		return c;
	}

	public Contractor remove(String contractorId) {
		Contractor c;

		for (Iterator<Contractor> it = contractors.iterator(); it.hasNext(); ) {
			c = it.next();
			if (c.getId().equalsIgnoreCase(contractorId)) {
				it.remove();
				return c;
			}
		}

		return null;
	}
}
