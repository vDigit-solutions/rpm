package com.vDigit.rpm.persistence.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.persistence.model.Privilege;

public interface PrivilegeRepository extends MongoRepository<Privilege, String> {

	Privilege findByName(String name);

	@Override
	void delete(Privilege privilege);

}
