package controllers;


public class EventBus {
	static com.google.common.eventbus.EventBus eventBus = new com.google.common.eventbus.EventBus();
	
	/**
	 * Subscribe to the global event bus.
	 * See: http://codingjunkie.net/guava-eventbus/
	 * @param subscriber
	 */
	public static void subscribe(Object subscriber) {
		eventBus.register(subscriber);
	}
	
	/**
	 * Post events to the event bus. Can be any Object.
	 * See: http://codingjunkie.net/guava-eventbus/
	 * @param event
	 */
	public static void post(Object event) {
		eventBus.post(event);
	}
}
