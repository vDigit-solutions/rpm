package com.vDigit.rpm.dto;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.vDigit.rpm.dao.ContractorDao;

@Component
public class Contractors {

	@Resource
	private ContractorDao contractorDao;

	public Collection<Contractor> getContractors() {
		return contractorDao.findAll();
	}

	public Contractor getContractor(String phone) {
		List<Contractor> contracors = contractorDao.findByPhoneLike(phone);
		if (CollectionUtils.isEmpty(contracors)) {
			return null;
		}
		return contracors.iterator().next();
	}

	public Contractor creatContractor(Contractor c) {
		// we should not make these assumptions here

//		Contractor saved = getContractor(c.getPhone());
//		if (saved != null) {
//			c.setId(saved.getId());
//		}
		Contractor saved = contractorDao.save(c);
		return saved;
	}

	public Contractor remove(String contractorId) {
		Contractor c = contractorDao.findOne(contractorId);
		contractorDao.delete(c);
		return c;
	}

	public Collection<Contractor> getContractors(String type) {
		return contractorDao.findByType(type);
	}

	public Contractor getContractorById(String contractorId) {
		return contractorDao.findOne(contractorId);
	}
}
