package com.vDigit.rpm.security;

public interface ISecurityUserService {

	String validatePasswordResetToken(String id, String token);

}
