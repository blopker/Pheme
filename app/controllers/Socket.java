package controllers;

import java.util.List;

import models.Component;
import models.Components;
import models.EventBus;
import models.datatypes.DataType;
import models.datatypes.DataTypes;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import ws.wamplay.annotations.URIPrefix;
import ws.wamplay.controllers.WAMPlayContoller;
import ws.wamplay.controllers.WAMPlayServer;

import com.google.common.eventbus.Subscribe;

@URIPrefix("pheme")
public class Socket extends WAMPlayContoller {

	public Socket() {
		WAMPlayServer.addTopic(getTopic("logs"));
		WAMPlayServer.addTopic(getTopic("components"));
		// Get in on the awesome event bus action.
		EventBus.subscribe(this);
	}

	@Subscribe
	public void componentListener(Component component) {
		if (!WAMPlayServer.isTopic(getTopic(component.id))) {
			WAMPlayServer.addTopic(getTopic(component.id));
		}
		publishComponents();
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

	private String getTopic(String id) {
		return "pheme." + id;
	}
}
