package pheme.api;

import pheme.api.dtos.CountDTO;
import pheme.api.dtos.DTO;
import pheme.api.dtos.GaugeDTO;
import pheme.api.dtos.LogDTO;

public class Component {
	final ComponentType type;
	final String componentName;
	final Pheme pheme;

	protected Component(String component, ComponentType type,
			Pheme pheme) {
		this.componentName = component;
		this.type = type;
		this.pheme = pheme;
	}

	public String getComponentName() {
		return componentName;
	}

	public boolean log(String type, String message) {
		return addToQueue(new LogDTO(this.componentName, this.type, type, message));
	}

	public boolean count(String counterName, long amountToAdd) {
		return addToQueue(new CountDTO(this.componentName, this.type, counterName,
				amountToAdd));
	}

	public boolean gauge(String gaugeName, long gaugeValue) {
		return addToQueue(new GaugeDTO(this.componentName, this.type, gaugeName,
				gaugeValue));
	}

	private synchronized boolean addToQueue(DTO dto) {
		return pheme.send(dto);
	}
}
