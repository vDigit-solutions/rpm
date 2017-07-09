package com.vDigit.rpm.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vDigit.rpm.dto.PropertyManager;
import com.vDigit.rpm.dto.PropertyManagers;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class PMsController {

	@Resource(name = "propertyManagers")
	private PropertyManagers propertyManagers = new PropertyManagers();

	@RequestMapping(value = "/pms", method = RequestMethod.GET)
	public @ResponseBody List<PropertyManager> getPMs() {
		return propertyManagers.getPropertyManagers();
	}

	@RequestMapping(value = "/pms", method = RequestMethod.POST)
	public @ResponseBody PropertyManager createPM(@RequestBody PropertyManager pmReq) {
		return propertyManagers.createPM(pmReq);
	}

	@RequestMapping(value = "/pms/{pmId}", method = RequestMethod.DELETE)
	public @ResponseBody PropertyManager deletePM(@PathVariable("pmId") String pmId) {
		return propertyManagers.removePM(pmId);
	}

}
