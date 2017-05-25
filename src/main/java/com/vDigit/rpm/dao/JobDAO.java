package com.vDigit.rpm.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.dto.Job;

public interface JobDAO extends MongoRepository<Job, String> {
	List<Job> findByPropertyManagerId(String pid);
}
