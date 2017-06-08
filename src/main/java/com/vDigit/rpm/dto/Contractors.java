package com.vDigit.rpm.dto;

import java.util.ArrayList;
import java.util.Collection;

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
		contractors.add(Contractor.makeNameAndPhone("100", "Siva", "2067904659"));
		contractors.add(Contractor.makeNameAndPhone("101", "Sasan", "4252837905"));
		contractors.add(Contractor.makeNameAndPhone("102", "Ramesh", "4259496967"));
		contractors.add(Contractor.makeNameAndPhone("103", "Keith", "3104089637"));
		return contractors;
	}

}
