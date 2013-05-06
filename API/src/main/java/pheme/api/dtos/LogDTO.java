package pheme.api.dtos;

import pheme.api.ComponentType;


public class LogDTO extends DTO {
	private static final long serialVersionUID = 7701625241344699527L;
	final String type;
	final String message;

	public LogDTO(String sender, ComponentType componentType, String type, String message) {
		super(sender, componentType, "log");
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
