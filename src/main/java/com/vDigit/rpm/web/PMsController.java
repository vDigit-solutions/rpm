package com.vDigit.rpm.web;

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
	List<PropertyManagers.PropertyManager> getPMs() {
		return propertyManagers.getPropertyManagers();
	}

//	@RequestMapping(value = "/contractors", method = RequestMethod.POST)
//	public @ResponseBody Contractor createContractor(@RequestBody Contractor contractorRequest) {
//		return contractors.creatContractor(contractorRequest);
//	}

}
