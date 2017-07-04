package com.vDigit.rpm.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PropertyManagers {
	private Map<String, PropertyManager> propertyManagerMap = makePropertyManagerMap();

	private Map<String, PropertyManager> makePropertyManagerMap() {
		Map<String, PropertyManager> map = new LinkedHashMap<String, PropertyManager>();
		PropertyManager pm = PropertyManager.makeNewPM("100", "Sasan", "2067904659");
//		PropertyManager pm = new PropertyManager("100", "3104089637", "KJ");
		map.put(pm.getId(), pm);
		return map;
	}

	public List<PropertyManager> getPropertyManagers() {
	    return new ArrayList<>(propertyManagerMap.values());
    }

	public PropertyManager getPropertyManagerName(String id) {
		return propertyManagerMap.get(id);
	}

    public PropertyManager createPM(PropertyManager pm) {
        propertyManagerMap.put(pm.getId(), pm);
        return pm;
    }

    public PropertyManager removePM(String pmId) {
	    PropertyManager pm = propertyManagerMap.get(pmId);
        propertyManagerMap.remove(pmId);
        return pm;
    }
}
