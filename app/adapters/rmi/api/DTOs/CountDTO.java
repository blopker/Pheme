package adapters.rmi.api.DTOs;

public class CountDTO implements DTO{
	private static final long serialVersionUID = -6466024814639298817L;
	final String sender;
	final String name;
	final long countToAdd;

	public CountDTO(String sender, String name, long countToAdd) {
		this.sender = sender;
		this.name = name;
		this.countToAdd = countToAdd;
	}
	
	public String getSender() {
		return sender;
	}

	public String getName() {
		return name;
	}

	public long getCountToAdd() {
		return countToAdd;
	}

	@Override
	public String getDataType() {
		return "count";
	}

}
