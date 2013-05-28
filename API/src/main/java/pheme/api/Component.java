package pheme.api;

import pheme.api.dtos.CountDTO;
import pheme.api.dtos.DTO;
import pheme.api.dtos.GaugeDTO;
import pheme.api.dtos.LogDTO;

public class Component {
	final ComponentType type;
	final String componentName;
	final Pheme pheme;

	protected Component(String component, ComponentType type, Pheme pheme) {
		this.componentName = component;
		this.type = type;
		this.pheme = pheme;
	}

	/**
	 * Returns the name of this component.
	 * 
	 * @return
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * Log a message for this component.
	 * 
	 * @param type
	 *            Type of log to commit (INFO, ERROR, WARNING)
	 * @param message
	 *            The actual log message.
	 * @return
	 */
	public boolean log(String type, String message) {
		return addToQueue(new LogDTO(this.componentName, this.type, type,
				message));
	}

	/**
	 * Increment the counter value of counterName. AmountToAdd can be positive
	 * or negative. If a counter by the name of counterName cannot be found a
	 * new counter is made, initialized at 0.
	 * 
	 * @param counterName
	 * @param amountToAdd
	 * @return
	 */
	public boolean count(String counterName, long amountToAdd) {
		return addToQueue(new CountDTO(this.componentName, this.type,
				counterName, amountToAdd));
	}

	/**
	 * Set the value of gauge gaugeName. The current value of the gauge will be
	 * set to gaugeValue.
	 * 
	 * @param gaugeName
	 * @param gaugeValue
	 * @return
	 */
	public boolean gauge(String gaugeName, long gaugeValue) {
		return addToQueue(new GaugeDTO(this.componentName, this.type,
				gaugeName, gaugeValue));
	}

	private synchronized boolean addToQueue(DTO dto) {
		return pheme.send(dto);
	}
}
