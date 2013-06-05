package models.datatypes;

import java.util.ArrayList;
import java.util.List;

import models.Component;

public enum DataTypes {
	COUNT,
	LOG,
	GAUGE;

	/**
	 * Overload of valueOf(String) to be case insensitive.
	 * @param dataType
	 * @return 
	 */
	public static DataTypes is(String dataType) {
		return DataTypes.valueOf(dataType.toUpperCase());
	}
	
	
	// Probably not the right place for this. Oh well.
	public static List<DataType> getAllFor(Component component) {
		List<DataType> datas = new ArrayList<DataType>();
		// Add each datatype manually... This could be done better.
		datas.addAll(filterByComponent(Count.getAll(), component));
		datas.addAll(filterByComponent(Log.getAll(), component));
		datas.addAll(filterByComponent(Gauge.getAll(), component));
		return datas;
	}
	
	private static List<DataType> filterByComponent(List<? extends DataType> data, Component comp) {
		List<DataType> datas = new ArrayList<DataType>();
		for (DataType dataType : data) {
			if (dataType.getComponent() == comp) {
				datas.add(dataType);
			}
		}
		return datas;
	}
}
