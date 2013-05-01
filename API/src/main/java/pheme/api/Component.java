package pheme.api;

import java.util.concurrent.BlockingQueue;

import pheme.api.dtos.CountDTO;
import pheme.api.dtos.DTO;
import pheme.api.dtos.GaugeDTO;
import pheme.api.dtos.LogDTO;

public class Component {
	final ComponentType type;
	final String componentName;
	final BlockingQueue<DTO> sendQueue;

	protected Component(String component, ComponentType type,
			BlockingQueue<DTO> messageQueue) {
		this.componentName = component;
		this.type = type;
		this.sendQueue = messageQueue;
	}

	public String getComponentName() {
		return componentName;
	}

	public void log(String type, String message) {
		addToQueue(new LogDTO(this.componentName, this.type, type, message));
	}

	public void count(String counterName, long amountToAdd) {
		addToQueue(new CountDTO(this.componentName, this.type, counterName,
				amountToAdd));
	}

	public void gauge(String gaugeName, long gaugeValue) {
		addToQueue(new GaugeDTO(this.componentName, this.type, gaugeName,
				gaugeValue));
	}

	private synchronized void addToQueue(DTO dto) {
		try {
			sendQueue.put(dto);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
