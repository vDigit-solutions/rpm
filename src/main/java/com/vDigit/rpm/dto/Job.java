package com.vDigit.rpm.dto;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Job {
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPropertyManagerId() {
		return propertyManagerId;
	}

	public void setPropertyManagerId(String propertyManagerId) {
		this.propertyManagerId = propertyManagerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDesiredDateOfBegin() {
		return desiredDateOfBegin;
	}

	public void setDesiredDateOfBegin(Date desiredDateOfBegin) {
		this.desiredDateOfBegin = desiredDateOfBegin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, ContractorEntry> getContractorEntries() {
		return contractorEntries;
	}

	public void setContractorEntries(Map<String, ContractorEntry> contractorEntries) {
		this.contractorEntries = contractorEntries;
	}

	public ContractWork getContractWork() {
		return contractWork;
	}

	public void setContractWork(ContractWork contractWork) {
		this.contractWork = contractWork;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	@Id
	private String id;
	private String propertyManagerId;
	private String description;
	private Date desiredDateOfBegin;
	private Date statusDate;
	private String status;
	// Painting, Carpet Cleaning, Electrical
	private String type;
	private String propertyName;

	private Map<String, ContractorEntry> contractorEntries = new LinkedHashMap<String, ContractorEntry>();
	private ContractWork contractWork;

	public void setContractorResponse(String id, String response) {
		ContractorEntry ce = contractorEntries.get(id);
		if (ce == null) {
			ce = new ContractorEntry();
			ce.id = id;
			contractorEntries.put(id, ce);

		}
		ce.response = response;
	}

	public static class ContractorEntry {
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}

		public Date getNotificationSentDate() {
			return notificationSentDate;
		}

		public void setNotificationSentDate(Date notificationSentDate) {
			this.notificationSentDate = notificationSentDate;
		}

		String id;
		String response;
		Date notificationSentDate;
	}

	public Job() {
	}

	public Contractor getPotentialNextContractor(Collection<Contractor> preferredContractors) {
		for (Contractor c : preferredContractors) {
			if (contractorEntries.containsKey(c.getId()))
				continue;
			return c;
		}
		return null;
	}

	public void addContractorEntry(ContractorEntry jce) {
		contractorEntries.put(jce.getId(), jce);
		this.currentContractorRequestId = jce.getId();
	}

	public ContractorEntry getContractorEntry(String id) {
		return contractorEntries.get(id);
	}

	private Address address;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getJobLocation() {
		if (getAddress() != null) {
			return getAddress().toString();
		}
		return "No Location specified";
	}

	// This can be replaced with ContractorEntry
	private String currentContractorRequestId;

	public String getCurrentContractorRequestId() {
		if (currentContractorRequestId != null) {
			return currentContractorRequestId;
		}
		return getLastContractorId();
	}

	private String getLastContractorId() {
		String x = null;
		for (String id : getContractorEntries().keySet()) {
			x = id;
		}
		return x;
	}

	public void setCurrentContractorRequestId(String currentContractorRequestId) {
		this.currentContractorRequestId = currentContractorRequestId;
	}

	public void updateContractorResponse(String contractorId, String response) {
		ContractorEntry ce = contractorEntries.get(contractorId);
		if (ce != null)
			ce.response = response;

	}

}
