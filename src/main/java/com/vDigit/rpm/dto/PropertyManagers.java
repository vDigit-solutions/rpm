package com.vDigit.rpm.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.vDigit.rpm.dao.ManagerDao;

@Component
public class PropertyManagers {

	private static Map<String, PropertyManager> propertyManagerMap = makePropertyManagerMap();

	@Resource
	private ManagerDao managerDao;

	private static Map<String, PropertyManager> makePropertyManagerMap() {
		Map<String, PropertyManager> map = new LinkedHashMap<String, PropertyManager>();
		PropertyManager pm = PropertyManager.makeNewPM("100", "Sasan", "2067904659");
		// PropertyManager pm = new PropertyManager("100", "3104089637", "KJ");
		map.put(pm.getId(), pm);
		return map;
	}

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

	public String message() {
		return "Hello World";
	}
	
	private String field = "Private Field in PropertyManager";
	
	private String getInfo(){
		return "Get Info";
	}
}
