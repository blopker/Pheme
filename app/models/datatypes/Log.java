package models.datatypes;

import java.util.*;

import javax.persistence.*;

import models.Component;
import models.EventBus;


import play.data.format.*;
import play.data.validation.*;
import play.db.ebean.Model;

@Entity
public class Log extends Model implements DataType{

	private static final long serialVersionUID = 1798964561277533958L;

	@Id
	@Constraints.Min(10)
	public String id = UUID.randomUUID().toString();

	@ManyToOne
	@Constraints.Required
	public Component component;

	@Constraints.Required
	public String logType;

	@Constraints.Required
	public String message;

	@Formats.DateTime(pattern = "dd/MM/yyyy")
	public Date created = new Date();

	private static Finder<String, Log> find = new Finder<String, Log>(String.class,
			Log.class);
	
	public static List<Log> getAll() {
		return Log.find.all();
	}

	public static DataType create(Component component, String type, String message) {
//		System.out.println("LOG: " + message);
		Log log = new Log();
		log.logType = type.toUpperCase();
		log.component = component;
		log.message = message.replace(System.getProperty("line.separator"), "<br/>\n");
		log.save();
		EventBus.post(log);
		return log;
	}

	@Override
	public DataTypes getDataType() {
		return DataTypes.LOG;
	}
}
