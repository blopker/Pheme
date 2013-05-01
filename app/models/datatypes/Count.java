package models.datatypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import models.Component;
import models.EventBus;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Count implements DataType{
	static Multimap<Component, Count> counts = HashMultimap.create();
	
	public String id = UUID.randomUUID().toString();
	public Component component;
	public String name;
	public long value;
	public Date created = new Date();

	public List<Count> getFor(Component component){
		List<Count> countList = new ArrayList<>();
		countList.addAll(counts.get(component));
		return countList;
	}
	
	public static List<Count> getAll() {
		List<Count> countList = new ArrayList<>();
		countList.addAll(counts.values());
		return countList;
	}
	
	public synchronized static DataType create(Component component, String counterName, long addToCount) {
		Count count = null;
		for (Count testCount : counts.get(component)) {
			if (testCount.name.equals(counterName)) {
				count = testCount;
			}
		}
		
		// Count doesn't exists, create.
		if (count == null) {
			count = new Count();
			count.name = counterName;
			count.component = component;
			count.value = 0;
			counts.put(component, count);
		}
		
		count.value += addToCount;
		
		EventBus.post(count);
		return count;
	}
	
	@Override
	public DataTypes getDataType() {
		return DataTypes.COUNT;
	}

	@Override
	public Component getComponent() {
		return component;
	}

}
