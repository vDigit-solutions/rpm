package com.vDigit.rpm.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vDigit.rpm.dto.GenericResponse;
import com.vDigit.rpm.dto.NotificationContext;
import com.vDigit.rpm.persistence.model.User;
import com.vDigit.rpm.persistence.model.VerificationToken;
import com.vDigit.rpm.registration.OnRegistrationCompleteEvent;
import com.vDigit.rpm.security.ISecurityUserService;
import com.vDigit.rpm.service.IUserService;
import com.vDigit.rpm.util.MailNotification;
import com.vDigit.rpm.web.dto.PasswordDto;
import com.vDigit.rpm.web.dto.UserDto;
import com.vDigit.rpm.web.error.InvalidOldPasswordException;

@Controller
public class RegistrationController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserService userService;

	@Autowired
	private ISecurityUserService securityUserService;

	@Autowired
	private MessageSource messages;

	@Resource(name = "mailNotification")
	private MailNotification mailNotification;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public RegistrationController() {
		super();
	}

	// Registration

	@RequestMapping(value = "/user/registration", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse registerUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request) {
		LOGGER.debug("Registering user account with information: {}", accountDto);

		final User registered = userService.registerNewUserAccount(accountDto);
		eventPublisher
				.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
		return new GenericResponse("success");
	}

	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration(final Locale locale, final Model model, @RequestParam("token") final String token)
			throws UnsupportedEncodingException {
		final String result = userService.validateVerificationToken(token);
		if (result.equals("valid")) {
			final User user = userService.getUser(token);
			if (user.isUsing2FA()) {
				model.addAttribute("qr", userService.generateQRUrl(user));
				return "redirect:/qrcode.html?lang=" + locale.getLanguage();
			}
			model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();
		}

		model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
		model.addAttribute("expired", "expired".equals(result));
		model.addAttribute("token", token);
		return "redirect:/badUser.html?lang=" + locale.getLanguage();
	}

	// user activation - verification

	@RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse resendRegistrationToken(final HttpServletRequest request,
			@RequestParam("token") final String existingToken) {
		final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
		final User user = userService.getUser(newToken.getToken());
		mailNotification
				.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
		return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
	}

	// Reset password
	@RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse resetPassword(final HttpServletRequest request,
			@RequestParam("email") final String userEmail) {
		final User user = userService.findUserByEmail(userEmail);
		if (user != null) {
			final String token = UUID.randomUUID().toString();
			userService.createPasswordResetTokenForUser(user, token);
			mailNotification.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
		}
		return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
	}

	@RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
	public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final String id,
			@RequestParam("token") final String token) {
		final String result = securityUserService.validatePasswordResetToken(id, token);
		if (result != null) {
			model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
			return "redirect:/login?lang=" + locale.getLanguage();
		}
		return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
	}

	@RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse savePassword(final Locale locale, @Valid PasswordDto passwordDto) {
		final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userService.changeUserPassword(user, passwordDto.getNewPassword());
		return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
	}

	// change user password
	@RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
		final User user = userService.findUserByEmail(
				((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
		if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
			throw new InvalidOldPasswordException();
		}
		userService.changeUserPassword(user, passwordDto.getNewPassword());
		return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
	}

	@RequestMapping(value = "/user/update/2fa", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse modifyUser2FA(@RequestParam("use2FA") final boolean use2FA)
			throws UnsupportedEncodingException {
		final User user = userService.updateUser2FA(use2FA);
		if (use2FA) {
			return new GenericResponse(userService.generateQRUrl(user));
		}
		return null;
	}

	// ============== NON-API ============

	private NotificationContext constructResendVerificationTokenEmail(final String contextPath, final Locale locale,
			final VerificationToken newToken, final User user) {
		final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
		final String message = messages.getMessage("message.resendToken", null, locale);
		return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
	}

	private NotificationContext constructResetTokenEmail(final String contextPath, final Locale locale,
			final String token, final User user) {
		final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
		final String message = messages.getMessage("message.resetPassword", null, locale);
		return constructEmail("Reset Password", message + " \r\n" + url, user);
	}

	private NotificationContext constructEmail(String subject, String body, User user) {
		final NotificationContext email = new NotificationContext();
		email.setSubject(subject);
		email.setMessage(body);
		email.setTo(user.getEmail());
		return email;
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

}
