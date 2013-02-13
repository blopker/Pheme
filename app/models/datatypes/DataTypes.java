package models.datatypes;

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
}
