package models.datatypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import models.Component;
import models.EventBus;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

public class Log implements DataType{
	
	static Multimap<Component, Log> logs = LinkedListMultimap.create();
	
	public String id = UUID.randomUUID().toString();
	public Component component;
	public String logType;
	public String message;
	public Date created = new Date();
	
	public static List<Log> getAll() {
		List<Log> logList = new ArrayList<>();
		logList.addAll(logs.values());
		return logList;
	}

	public static DataType create(Component component, String type, String message) {
		Log log = new Log();
		log.logType = type.toUpperCase();
		log.component = component;
		log.message = message.replace(System.getProperty("line.separator"), "<br/>\n");
		logs.put(component, log);
		EventBus.post(log);
		return log;
	}

	@Override
	public DataTypes getDataType() {
		return DataTypes.LOG;
	}
}
