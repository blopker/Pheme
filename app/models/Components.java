package models;

public enum Components {
	JOB,
	COMPUTER;

//	public String toString() {
//		return this.toString().toUpperCase();
//	}
	
	/**
	 * Overload of valueOf(String) to be case insensitive.
	 * @param dataType
	 * @return 
	 */
	public static Components is(String component) {
		return Components.valueOf(component.toUpperCase());
	}
}
