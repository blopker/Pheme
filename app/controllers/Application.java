package controllers;

import java.util.List;

import models.Component;
import models.Components;
import models.clients.ClientManager;
import models.clients.SocketClient;
import models.datatypes.Log;

import org.codehaus.jackson.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.component;
import views.html.componentsList;
import views.html.index;
import views.html.*;

public class Application extends Controller {


	
    /**
     * Display the start page.
     */
    public static Result index() {
        return ok(index.render());
    }

    /**
     * Display all the logs.
     */
    public static Result logs() {
    	List<Log> logList = Log.getAll();
        return ok(logs.render(logList));
    }
    
    /**
     * Get a list of all the running jobs
     */
    public static Result jobList() {
    	List<Component> jobs = Component.getAll(Components.JOB);
		return ok(componentsList.render(jobs, "Jobs"));
	}
    
    /**
     * Inspect a specific job
     */
    public static Result job(String id) {
    	Component job = Component.get(id);
    	if (job == null) {
			return notFound("<h1>Page not found</h1>").as("text/html");
		}
    	return ok(component.render(job));
	}
    
    /**
     * Get a list of all the running computers
     */
    public static Result computerList() {
    	List<Component> computers = Component.getAll(Components.COMPUTER);
		return ok(componentsList.render(computers, "Computers"));
	}
    
    /**
     * Inspect a specific computer
     */
    public static Result computer(String id) {
    	Component computer = Component.get(id);
    	if (computer == null) {
			return notFound("<h1>Page not found</h1>").as("text/html");
		}
		return ok(component.render(computer));
	}

    /**
     * Handle the websocket.
     */
    public static WebSocket<JsonNode> socket() {
        return new WebSocket<JsonNode>() {

            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){
            	SocketClient client = new SocketClient(in, out);
            	ClientManager.addClient(client);
            }
        };
    }
}
