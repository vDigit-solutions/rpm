package com.vDigit.rpm.persistence.dao;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vDigit.rpm.persistence.model.PasswordResetToken;
import com.vDigit.rpm.persistence.model.User;

public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {

	PasswordResetToken findByToken(String token);

	PasswordResetToken findByUser(User user);

	Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

	void deleteByExpiryDateLessThan(Date now);

}
