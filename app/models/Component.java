package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Component {
	static Map<String, Component> components = new HashMap<String, Component>();
	
	public String id = UUID.randomUUID().toString();
	public String componentName;
	public Components componentType;
	public Date created = new Date();
	
	public static List<Component> getAll(Components type) {
		List<Component> componentList = new ArrayList<>();
		for (Component component : components.values()) {
			if (component.componentType.equals(type)) {
				componentList.add(component);
			}
		}
		return componentList;
	}
	
	public synchronized static Component findOrCreate(String componentName, Components type) {
		Component component = null;
		
		for (Component testComponent : components.values()) {
			if (testComponent.componentName.equals(componentName) && testComponent.componentType.equals(type)) {
				component = testComponent;
				break;
			}
		}
		
		// Component not found, create
		if (component == null) {
			component = new Component();
			component.componentName = componentName;
			component.componentType = type;
			components.put(component.id, component);
		}
		
		return component;
	}
	
	public static Component get(String id) {
		return components.get(id);
	}
	
	public static int getCount(Components type){
		return Component.getAll(type).size();
	}

}
