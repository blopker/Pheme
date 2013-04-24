package pheme.api.dtos;

import pheme.api.ComponentType;

public class CountDTO extends DTO{
	private static final long serialVersionUID = -6466024814639298817L;
	final String name;
	final long countToAdd;

	public CountDTO(String sender, ComponentType componentType, String name, long countToAdd) {
		super(sender, componentType, "count");
		this.name = name;
		this.countToAdd = countToAdd;
	}

	public String getName() {
		return name;
	}

	public long getCountToAdd() {
		return countToAdd;
	}
}
