package com.vDigit.rpm.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.dto.PropertyManager;

public interface ManagerDao extends MongoRepository<PropertyManager, String> {
	List<PropertyManager> findByPhoneLike(String phone);
}
