package models.datatypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import models.Component;
import models.EventBus;

public class Gauge implements DataType{
static Multimap<Component, Gauge> gauges = HashMultimap.create();
	
	public String id = UUID.randomUUID().toString();
	public Component component;
	public String name;
	public long value;
	public Date created = new Date();

	public List<Gauge> getFor(Component component){
		List<Gauge> gaugeList = new ArrayList<>();
		gaugeList.addAll(gauges.get(component));
		return gaugeList;
	}
	
	public static List<Gauge> getAll() {
		List<Gauge> gaugeList = new ArrayList<>();
		gaugeList.addAll(gauges.values());
		return gaugeList;
	}
	
	public synchronized static DataType create(Component component, String gaugeName, long gaugeValue) {
		Gauge gauge = null;
		for (Gauge testGuage : gauges.get(component)) {
			if (testGuage.name.equals(gaugeName)) {
				gauge = testGuage;
			}
		}
		
		// Gauge doesn't exists, create.
		if (gauge == null) {
			gauge = new Gauge();
			gauge.name = gaugeName;
			gauge.component = component;
			gauge.value = 0;
			gauges.put(component, gauge);
		}
		
		gauge.value = gaugeValue;
		
		EventBus.post(gauge);
		return gauge;
	}
	
	@Override
	public Component getComponent() {
		return component;
	}
	@Override
	public DataTypes getDataType() {
		return DataTypes.GAUGE;
	}
}
