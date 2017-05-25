package com.vDigit.rpm.dto;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.vDigit.rpm.dto.Job.ContractorEntry;

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

	@Id
	private String id;
	private String propertyManagerId;
	private String description;
	private Date desiredDateOfBegin;
	// Painting, Carpet Cleaning, Electrical
	private String type;
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

	public Contractor getPotentialNextContracter(Collection<Contractor> preferredContractors) {
		for (Contractor c : preferredContractors) {
			if (contractorEntries.containsKey(c.getId()))
				continue;
			return c;
		}
		return null;
	}

	public void addContractorEntry(ContractorEntry jce) {
		contractorEntries.put(jce.getId(), jce);
	}

	public ContractorEntry getContractorEntry(String id) {
		return contractorEntries.get(id);
	}
}
