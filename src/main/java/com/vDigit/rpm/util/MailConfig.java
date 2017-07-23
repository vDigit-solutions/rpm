package com.vDigit.rpm.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sendgrid.SendGrid;

@Configuration
public class MailConfig {
	@Value("${app.key:U0cublpuOTFRNG5RMHFlNmJnQlZneUhwUS42eTlTSWVyTVhiZkpLRDJsZExyM0E5dTNKRXlPUU1UdjhmOXIwenpSejBB}")
	private String appKey;

	@Bean
	public SendGrid sendGrid() throws Exception {
		String apiKey = new String(Base64.decodeBase64(appKey));
		return new SendGrid(apiKey);
	}
}
