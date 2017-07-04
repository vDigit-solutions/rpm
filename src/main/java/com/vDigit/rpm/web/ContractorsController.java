package com.vDigit.rpm.web;

import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.Contractors;
import org.springframework.web.bind.annotation.*;

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

	@RequestMapping(value = "/contractors", method = RequestMethod.POST)
	public @ResponseBody Contractor createContractor(@RequestBody Contractor contractorRequest) {
		return contractors.creatContractor(contractorRequest);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "Test..";
	}
}
