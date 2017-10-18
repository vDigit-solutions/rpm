package com.vDigit.rpm.persistence.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.persistence.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	User findByEmail(String email);

	@Override
	void delete(User user);

}
