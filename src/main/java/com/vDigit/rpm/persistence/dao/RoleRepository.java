package com.vDigit.rpm.persistence.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.persistence.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

	Role findByName(String name);

	@Override
	void delete(Role role);

}
