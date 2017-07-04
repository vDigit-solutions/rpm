package com.vDigit.rpm.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PropertyManagers {
	private Map<String, PropertyManager> propertyManagerMap = makePropertyManagerMap();

	private Map<String, PropertyManager> makePropertyManagerMap() {
		Map<String, PropertyManager> map = new LinkedHashMap<String, PropertyManager>();
		PropertyManager pm = new PropertyManager("100", "2067904659", "Sasan");
//		PropertyManager pm = new PropertyManager("100", "3104089637", "KJ");
		map.put(pm.id, pm);
		return map;
	}

	public List<PropertyManager> getPropertyManagers() {
	    return new ArrayList<>(propertyManagerMap.values());
    }

	public PropertyManager getPropertyManagerName(String id) {
		return propertyManagerMap.get(id);
	}

	public static class PropertyManager {
		private String id;
		private String phone;
		private String name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		PropertyManager(String id, String phone, String name) {
			this.id = id;
			this.phone = phone;
			this.name = name;
		}
	}
}
