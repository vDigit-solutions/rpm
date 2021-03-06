package com.vDigit.rpm.web;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vDigit.rpm.dto.Contractor;
import com.vDigit.rpm.dto.Contractors;
import com.vDigit.rpm.persistence.model.User;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class ContractorsController {

	@Resource(name = "contractors")
	private Contractors contractors;

	@RequestMapping(value = "/contractors", method = RequestMethod.GET)
	public @ResponseBody Collection<Contractor> getContractors(Authentication authentication) {
		User user = getUser(authentication);
		return contractors.getContractorsByManager(user.getId());
	}

	private User getUser(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return user;
	}

	@RequestMapping(value = "/contractors", method = RequestMethod.POST)
	public @ResponseBody Contractor createContractor(@RequestBody Contractor contractorRequest,
			Authentication authentication) {
		contractorRequest.setManagerId(getUser(authentication).getId());
		return contractors.creatContractor(contractorRequest);
	}

	@RequestMapping(value = "/contractors/{contractorId}", method = RequestMethod.DELETE)
	public @ResponseBody Contractor deletePM(@PathVariable("contractorId") String contractorId) {
		return contractors.remove(contractorId);
	}

}
