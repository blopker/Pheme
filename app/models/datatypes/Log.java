package models.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import models.Component;
import models.EventBus;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

public class Log implements DataType{
	
	static Multimap<Component, Log> logs = LinkedListMultimap.create();
	
	static int MAX_LOGS_PER_COMPONENT = 1000;
	
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

	public synchronized static DataType create(Component component, String type, String message) {
		Log log = new Log();
		log.logType = type.toUpperCase();
		log.component = component;
		log.message = message;
		logs.put(component, log);
		cleanup(component);
		EventBus.post(log);
		return log;
	}

	private static void cleanup(Component component) {
		Collection<Log> logColl = logs.get(component);		
		for (Log log : logColl) {
			if (logColl.size() > MAX_LOGS_PER_COMPONENT) {
				logColl.remove(log);
			} else {
				return;
			}
		}
	}

	@Override
	public DataTypes getDataType() {
		return DataTypes.LOG;
	}

	@Override
	public Component getComponent() {
		return component;
	}
}
