package adapters.rmi.api;

import java.util.concurrent.BlockingQueue;

import adapters.rmi.api.DTOs.CountDTO;
import adapters.rmi.api.DTOs.DTO;
import adapters.rmi.api.DTOs.LogDTO;

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
		try {
			sendQueue.put(new LogDTO(this.componentName, this.type, type,
					message));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void count(String counterName, long amountToAdd) {
		try {
			sendQueue.put(new CountDTO(this.componentName, this.type,
					counterName, amountToAdd));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
