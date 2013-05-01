package pheme.api.dtos;

import pheme.api.ComponentType;

public class GaugeDTO extends DTO{
	private static final long serialVersionUID = -6466024814639298817L;
	final String name;
	final long gaugeValue;

	public GaugeDTO(String sender, ComponentType componentType, String name, long gaugeValue) {
		super(sender, componentType, "gauge");
		this.name = name;
		this.gaugeValue = gaugeValue;
	}

	public String getName() {
		return name;
	}

	public long getGaugeValue() {
		return gaugeValue;
	}
}
