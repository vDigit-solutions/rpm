package com.vDigit.rpm.util;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.vDigit.rpm.dto.NotificationContext;

@Component
public class MailNotification implements Notification<NotificationContext, String> {
	private Logger logger = LoggerFactory.getLogger(MailNotification.class);
	@Resource
	private SendGrid sendGrid;

	@Override
	public String send(NotificationContext input) {
		Email from = new Email("donotreply@rpm.com");
		Email to = new Email(input.getTo());
		Content content = new Content("text/html", input.getMessage());
		Mail mail = new Mail(from, input.getSubject(), to, content);
		mail.setTemplateId("40715bb4-0633-43dc-a572-538018eaef30");
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sendGrid.api(request);
			logger.info(response.getStatusCode() + "");
			return response.getStatusCode() + "";
		} catch (IOException ex) {
			logger.error("Exception while sending email..", ex);
			throw new RuntimeException(ex);
		}
	}

}
