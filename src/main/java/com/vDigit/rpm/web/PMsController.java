package com.vDigit.rpm.web;

import com.vDigit.rpm.dto.PropertyManager;
import com.vDigit.rpm.dto.PropertyManagers;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class PMsController {
	private PropertyManagers propertyManagers = new PropertyManagers();

	@RequestMapping(value = "/pms", method = RequestMethod.GET)
	public @ResponseBody
	List<PropertyManager> getPMs() {
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
