package com.vDigit.rpm.dto;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.vDigit.rpm.dao.ManagerDao;

@Component
public class PropertyManagers {

	@Resource
	private ManagerDao managerDao;

	public List<PropertyManager> getPropertyManagers() {
		return managerDao.findAll();
	}

	public PropertyManager getPropertyManager(String id) {
		return managerDao.findOne(id);
	}

	public PropertyManager createPM(PropertyManager pm) {
		// propertyManagerMap.put(pm.getId(), pm);
		List<PropertyManager> manager = managerDao.findByPhoneLike(pm.getPhone());
		if (!CollectionUtils.isEmpty(manager)) {
			pm.setId(manager.iterator().next().getId());
		}
		PropertyManager saved = managerDao.save(pm);
		return saved;
	}

	public PropertyManager removePM(String pmId) {
		// PropertyManager pm = propertyManagerMap.get(pmId);
		// propertyManagerMap.remove(pmId);
		PropertyManager pm = getPropertyManager(pmId);
		managerDao.delete(pm);
		return pm;
	}
}
