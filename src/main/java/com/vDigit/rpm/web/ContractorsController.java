package com.vDigit.rpm.web;

import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.Contractors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class ContractorsController {
    private Contractors contractors = new Contractors();

	@RequestMapping(value = "/contractors", method = RequestMethod.GET)
	public @ResponseBody
    Collection<Contractor> getContractors() {
        return contractors.getContractors();
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "Test..";
	}
}
