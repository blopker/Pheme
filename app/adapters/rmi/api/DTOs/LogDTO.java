package adapters.rmi.api.DTOs;


public class LogDTO implements DTO {
	private static final long serialVersionUID = 7838274700757079439L;
	final String client;
	final String type;
	final String message;

	public LogDTO(String sender, String type, String message) {
		this.client = sender;
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String getDataType() {
		return "log";
	}

	@Override
	public String getSenderName() {
		return client;
	}

	@Override
	public String getSenderType() {
		// TODO Auto-generated method stub
		return null;
	}
}
