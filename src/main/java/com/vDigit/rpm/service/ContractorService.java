package com.vDigit.rpm.service;

import com.vDigit.rpm.dto.ContractorRequest;

public interface ContractorService {
	void notifyContractor(ContractorRequest request);

	void processContractorResponse(ContractorRequest request);
}
