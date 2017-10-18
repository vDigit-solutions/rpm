package com.vDigit.rpm.registration.listener;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.persistence.model.User;
import com.vDigit.rpm.registration.OnRegistrationCompleteEvent;
import com.vDigit.rpm.service.IUserService;
import com.vDigit.rpm.util.MailNotification;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	@Autowired
	private IUserService service;

	@Autowired
	private MessageSource messages;

	@Resource(name = "mailNotification")
	private MailNotification mailNotification;

	// API

	@Override
	public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(final OnRegistrationCompleteEvent event) {
		final User user = event.getUser();
		final String token = UUID.randomUUID().toString();
		service.createVerificationTokenForUser(user, token);

		NotificationContext email = new NotificationContext();
		email.setTo(user.getEmail());
		final String subject = "Registration Confirmation";
		final String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
		final String message = messages.getMessage("message.regSucc", null, event.getLocale());
		email.setSubject(subject);
		email.setMessage(message + " \r\n" + confirmationUrl);
		mailNotification.send(email);
	}

}
