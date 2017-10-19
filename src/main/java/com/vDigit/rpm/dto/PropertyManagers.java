package com.vDigit.rpm.dto;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.vDigit.rpm.persistence.model.User;
import com.vDigit.rpm.service.IUserService;

@Component
public class PropertyManagers {

	@Resource(name = "userService")
	private IUserService managerDao;

	public PropertyManager getPropertyManager(String id) {
		User user = managerDao.getUserByID(id);
		return new PropertyManager(user);
	}

}
