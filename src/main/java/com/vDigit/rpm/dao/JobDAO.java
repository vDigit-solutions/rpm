package com.vDigit.rpm.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.dto.Job;

public interface JobDAO extends MongoRepository<Job, String> {
	List<Job> findByPropertyManagerId(String pid);

	List<Job> findByCurrentContractorRequestId(String id);

	List<Job> findByStatus(String status);

	List<Job> findByStatusAndStatusDateLessThanEqual(String status, Date date);
}
