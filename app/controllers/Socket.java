package controllers;

import java.util.List;

import models.Component;
import models.Components;
import models.EventBus;
import models.datatypes.DataType;
import models.datatypes.DataTypes;
import models.datatypes.Log;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import ws.wamplay.annotations.URIPrefix;
import ws.wamplay.annotations.onSubscribe;
import ws.wamplay.callbacks.PubSubCallback;
import ws.wamplay.controllers.WAMPlayContoller;
import ws.wamplay.controllers.WAMPlayServer;
import ws.wamplay.models.WAMPlayClient;
import ws.wamplay.models.messages.Event;

import com.google.common.eventbus.Subscribe;

@URIPrefix("pheme.")
public class Socket extends WAMPlayContoller {

	public Socket() {
		this.addTopic("components");
		// Get in on the awesome event bus action.
		EventBus.subscribe(this);
	}

	@Subscribe
	public void componentListener(final Component component) {
		addComponentTopic(component);
		publishComponents();
	}
	
	private void addComponentTopic(final Component component) {
		final String componentTopic = getTopic(component.id);
		if (!WAMPlayServer.isTopic(componentTopic)) {
			WAMPlayServer.addTopic(componentTopic);
			WAMPlayServer.addTopic(componentTopic, new PubSubCallback(){
				@Override
				protected boolean onSubscribe(String sessionID) {
					// Send all the old data to the new client right away.
					WAMPlayClient client = WAMPlayServer.getClient(sessionID);
					List<DataType> datas = DataTypes.getAllFor(component);
					for (DataType data : datas) {
						client.send(new Event(componentTopic, Json.toJson(data)).toJson());
					}
					return true;
				}
			});
		}
	}
	
	private void publishComponents() {
		ObjectNode result = Json.newObject();
		List<Component> computers = Component.getAll(Components.COMPUTER);
		List<Component> jobs = Component.getAll(Components.JOB);
		result.put("computerCount", computers.size());
		result.put("jobCount", jobs.size());
		result.put("jobs", Json.toJson(jobs));
		result.put("computers", Json.toJson(computers));
		WAMPlayServer.publish(getTopic("components"), result);
	}

	@Subscribe
	public void dataListener(DataType data) {
		JsonNode d = Json.toJson(data);
		if (data.getDataType() == DataTypes.LOG) {
			WAMPlayServer.publish(getTopic("logs"), d);
		}
		WAMPlayServer.publish(getTopic(data.getComponent().id), d);
	}
	
	@onSubscribe("logs")
	public boolean logsSubscribe(String sessionID){
		String topic = getTopic("logs");
		// Send all the old logs to the new client right away.
		WAMPlayClient client = WAMPlayServer.getClient(sessionID);
		List<Log> datas = Log.getAll();
		for (DataType data : datas) {
			client.send(new Event(topic, Json.toJson(data)).toJson());
		}
		return true;
	}

	private String getTopic(String id) {
		return "pheme." + id;
	}
}
