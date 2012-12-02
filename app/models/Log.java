package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

@Entity
public class Log extends Model {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum Level {
		INFO, DEBUG, WARN, ERROR
	}

	@Id
	@Constraints.Min(10)
	public String id = UUID.randomUUID().toString();

	@Constraints.Required
	public String sourceName;

	@Constraints.Required
	public Level level;

	@Constraints.Required
	public String message;

	@Formats.DateTime(pattern = "dd/MM/yyyy")
	public Date created = new Date();

	public static Finder<Long, Log> find = new Finder<Long, Log>(Long.class,
			Log.class);

	public static Log create(String sourceName, Level level, String message) {
		System.out.println("LOG: " + message);
		Log log = new Log();
		log.level = level;
		log.sourceName = sourceName;
		log.message = message;
		log.save();
		Application.post(log);
		return log;
	}
}
