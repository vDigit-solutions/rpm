package com.vDigit.rpm.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.vDigit.rpm.persistence.model.PasswordResetToken;
import com.vDigit.rpm.persistence.model.User;
import com.vDigit.rpm.persistence.model.VerificationToken;
import com.vDigit.rpm.web.dto.UserDto;
import com.vDigit.rpm.web.error.UserAlreadyExistException;

public interface IUserService {

	User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

	User getUser(String verificationToken);

	void saveRegisteredUser(User user);

	void deleteUser(User user);

	void createVerificationTokenForUser(User user, String token);

	VerificationToken getVerificationToken(String VerificationToken);

	VerificationToken generateNewVerificationToken(String token);

	void createPasswordResetTokenForUser(User user, String token);

	User findUserByEmail(String email);

	PasswordResetToken getPasswordResetToken(String token);

	User getUserByPasswordResetToken(String token);

	User getUserByID(String id);

	void changeUserPassword(User user, String password);

	boolean checkIfValidOldPassword(User user, String password);

	String validateVerificationToken(String token);

	String generateQRUrl(User user) throws UnsupportedEncodingException;

	User updateUser2FA(boolean use2FA);

	List<String> getUsersFromSessionRegistry();

}
