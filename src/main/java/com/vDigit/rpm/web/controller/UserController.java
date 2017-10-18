package com.vDigit.rpm.web.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vDigit.rpm.security.ActiveUserStore;
import com.vDigit.rpm.service.IUserService;

@Controller
public class UserController {

	@Autowired
	ActiveUserStore activeUserStore;

	@Autowired
	IUserService userService;

	@RequestMapping(value = "/loggedUsers", method = RequestMethod.GET)
	public String getLoggedUsers(final Locale locale, final Model model) {
		model.addAttribute("users", activeUserStore.getUsers());
		return "users";
	}

	@RequestMapping(value = "/loggedUsersFromSessionRegistry", method = RequestMethod.GET)
	public String getLoggedUsersFromSessionRegistry(final Locale locale, final Model model) {
		model.addAttribute("users", userService.getUsersFromSessionRegistry());
		return "users";
	}
}
