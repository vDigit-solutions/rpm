package com.vDigit.rpm.dto;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Job {
	public static class ContractorEntry {
		String id;

		Date notificationSentDate;

		String response;

		public String getId() {
			return id;
		}

		public Date getNotificationSentDate() {
			return notificationSentDate;
		}

		public String getResponse() {
			return response;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setNotificationSentDate(Date notificationSentDate) {
			this.notificationSentDate = notificationSentDate;
		}

		public void setResponse(String response) {
			this.response = response;
		}
	}

	private Address address;

	private Map<String, ContractorEntry> contractorEntries = new LinkedHashMap<String, ContractorEntry>();

	private ContractWork contractWork;

	@CreatedDate
	private Date createdDate;

	// This can be replaced with ContractorEntry
	private String currentContractorRequestId;

	private String description;

	private Date desiredDateOfBegin;

	@Id
	private String id;

	private String propertyManagerId;

	private String propertyName;

	private String status;

	private Date statusDate;

	// Painting, Carpet Cleaning, Electrical
	private String type;

	public Job() {
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void addContractorEntry(ContractorEntry jce) {
		contractorEntries.put(jce.getId(), jce);
		this.currentContractorRequestId = jce.getId();
	}

	public Address getAddress() {
		return address;
	}

	public Map<String, ContractorEntry> getContractorEntries() {
		return contractorEntries;
	}

	public ContractorEntry getContractorEntry(String id) {
		return contractorEntries.get(id);
	}

	public ContractWork getContractWork() {
		return contractWork;
	}

	public String getCurrentContractorRequestId() {
		if (currentContractorRequestId != null) {
			return currentContractorRequestId;
		}
		return getLastContractorId();
	}

	public String getDescription() {
		return description;
	}

	public Date getDesiredDateOfBegin() {
		return desiredDateOfBegin;
	}

	public String getId() {
		return id;
	}

	public String getJobLocation() {
		if (getAddress() != null) {
			return getAddress().toString();
		}
		return "No Location specified";
	}

	private String getLastContractorId() {
		String x = null;
		for (String id : getContractorEntries().keySet()) {
			x = id;
		}
		return x;
	}

	public Contractor getPotentialNextContractor(Collection<Contractor> preferredContractors) {
		for (Contractor c : preferredContractors) {
			if (contractorEntries.containsKey(c.getId()))
				continue;
			return c;
		}
		return null;
	}

	public String getPropertyManagerId() {
		return propertyManagerId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getStatus() {
		return status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public String getType() {
		return type;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setContractorEntries(Map<String, ContractorEntry> contractorEntries) {
		this.contractorEntries = contractorEntries;
	}

	public void setContractorResponse(String id, String response) {
		ContractorEntry ce = contractorEntries.get(id);
		if (ce == null) {
			ce = new ContractorEntry();
			ce.id = id;
			contractorEntries.put(id, ce);

		}
		ce.response = response;
	}

	public void setContractWork(ContractWork contractWork) {
		this.contractWork = contractWork;
	}

	public void setCurrentContractorRequestId(String currentContractorRequestId) {
		this.currentContractorRequestId = currentContractorRequestId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDesiredDateOfBegin(Date desiredDateOfBegin) {
		this.desiredDateOfBegin = desiredDateOfBegin;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPropertyManagerId(String propertyManagerId) {
		this.propertyManagerId = propertyManagerId;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void updateContractorResponse(String contractorId, String response) {
		ContractorEntry ce = contractorEntries.get(contractorId);
		if (ce != null)
			ce.response = response;

	}

}
