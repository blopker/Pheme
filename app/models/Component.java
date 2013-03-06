package models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
public class Component extends Model {
	private static final long serialVersionUID = -269431700600617384L;

	@Id
	@Constraints.Min(10)
	public String id = UUID.randomUUID().toString();

	@Constraints.Required
	public String componentName;
	
	@Constraints.Required
	public Components componentType;

	@Constraints.Required
	@Formats.DateTime(pattern = "dd/MM/yyyy")
	public Date created = new Date();

	private static Finder<String, Component> find = new Finder<String, Component>(String.class,
			Component.class);
	
	public static List<Component> getAll(Components type) {
		return Component.find.where().eq("componentType", type).findList();
	}
	
	public static Component findOrCreate(String componentName, Components type) {
		Component component = Component.find.where().eq("componentName", componentName).eq("componentType", type).findUnique();
		if (component == null) {
			component = new Component();
			component.componentName = componentName;
			component.componentType = type;
			component.save();
		}
//		System.out.println(component.componentType);
		return component;
	}
	
	public static Component get(String id) {
		return Component.find.byId(id);
	}
	
	public static int getCount(Components type){
		return Component.getAll(type).size();
	}

}
