package com.vDigit.rpm.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.dto.Contractor;

public interface ContractorDao extends MongoRepository<Contractor, String> {
	List<Contractor> findByPhoneLike(String phone);

	List<Contractor> findByTypeAndManagerId(String type, String managerId);

	List<Contractor> findByManagerId(String managerId);
}
