package models.datatypes;

import java.util.ArrayList;
import java.util.List;

import models.Component;

public enum DataTypes {
	COUNT("count"),
	LOG("log");
	
	private final String type;
	DataTypes(String type){
		this.type = type;
	}
	
	public String toString() {
		return this.type;
	}
	
	/**
	 * Overload of valueOf(String) to be case insensitive.
	 * @param dataType
	 * @return 
	 */
	public static DataTypes is(String dataType) {
		return DataTypes.valueOf(dataType.toUpperCase());
	}
	
	public static List<DataType> getAllFor(Component component) {
		List<DataType> datas = new ArrayList<DataType>();
		datas.addAll(filterByComponent(Count.getAll(), component));
		datas.addAll(filterByComponent(Log.getAll(), component));
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
