package adapters.rmi.api;

public class LogRMI implements MessageRMI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name = "log";
	final String client;
	final String type;
	final String message;

	public LogRMI(String sender, String type, String message) {
		this.client = sender;
		this.type = type;
		this.message = message;
	}

	public String getClient() {
		return client;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String getSender() {
		return client;
	}

}
