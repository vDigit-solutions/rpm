package com.vDigit.rpm.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.dto.PropertyManager;

public interface ManagerDao extends MongoRepository<PropertyManager, String> {

}
