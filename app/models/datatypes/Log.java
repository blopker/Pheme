package models.datatypes;

import java.util.*;

import javax.persistence.*;

import controllers.EventBus;

import play.data.format.*;
import play.data.validation.*;
import play.db.ebean.Model;

@Entity
public class Log extends Model implements DataType{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1798964561277533958L;

	private static String dataType = "log";
	
	@Id
	@Constraints.Min(10)
	public String id = UUID.randomUUID().toString();

	@Constraints.Required
	public String sourceName;

	@Constraints.Required
	public String logType;

	@Constraints.Required
	public String message;

	@Formats.DateTime(pattern = "dd/MM/yyyy")
	public Date created = new Date();

	public static Finder<Long, Log> find = new Finder<Long, Log>(Long.class,
			Log.class);

	public static DataType create(String sourceName, String type, String message) {
//		System.out.println("LOG: " + message);
		Log log = new Log();
		log.logType = type.toUpperCase();
		log.sourceName = sourceName;
		log.message = message;
		log.save();
		EventBus.post(log);
		return log;
	}

	@Override
	public String getDataType() {
		return dataType;
	}
}
