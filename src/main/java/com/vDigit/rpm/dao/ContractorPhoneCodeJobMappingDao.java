package com.vDigit.rpm.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.dto.ContractorPhoneCodeJob;

public interface ContractorPhoneCodeJobMappingDao extends MongoRepository<ContractorPhoneCodeJob, String> {

	List<ContractorPhoneCodeJob> findByYesOrNo(Integer yes, Integer code);

	List<ContractorPhoneCodeJob> findByJobId(String jobId);

	List<ContractorPhoneCodeJob> findByJobIdAndContractorId(String jobId, String contractorId);

}
