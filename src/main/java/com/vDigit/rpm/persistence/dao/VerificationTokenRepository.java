package com.vDigit.rpm.persistence.dao;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.persistence.model.User;
import com.vDigit.rpm.persistence.model.VerificationToken;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {

	VerificationToken findByToken(String token);

	VerificationToken findByUser(User user);

	Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

	void deleteByExpiryDateLessThan(Date now);

}
