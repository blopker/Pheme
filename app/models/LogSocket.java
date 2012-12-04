package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import com.google.common.eventbus.Subscribe;

import controllers.EventBus;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.WebSocket;

/**
 * A WebSocket log viewer. Clients will get push updates of new logs and can request specific log types.
 */
public class LogSocket {
	// Create a persistent log socket to track clients.
	static LogSocket logSocket = new LogSocket();
    
    // Client list. Client is a user of the web interface.
    private List<Client> clients = Collections.synchronizedList(new ArrayList<Client>());
    
    
    // Create a Robot, just for fun.
    static {
        new Robot();
    }
    
    public LogSocket() {
    	// Get in on the awesome event bus action.
    	EventBus.subscribe(this);
    }
    
    /**
     * Connect/register a client to the current Log Socket instance.
     */
    public static void connect(WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) throws Exception{
    	logSocket.connectImpl(in, out);
    }
    
    @Subscribe
    public void logListener(Log log){
    	notifyAll(Json.toJson(log));
    }
    
    private void connectImpl(WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
    	final Client client = new Client(out);
    	clients.add(client);
    	ObjectNode result = Json.newObject();
    	result.put("message", "Place holder for all previous logs.");
    	out.write(result);
    	
    	in.onMessage(new Callback<JsonNode>() {
			
			@Override
			public void invoke(JsonNode a) throws Throwable {
				JsonNode resp = getResponse(a);
				out.write(resp);				
			}
		});
    	
    	in.onClose(new Callback0() {
			
			@Override
			public void invoke() throws Throwable {
				// Remove client from our active client list.
				out.close();
				clients.remove(client);
			}
		});       
	}
    
    
    private JsonNode getResponse(JsonNode json){
    	if(json == null){
    		return getAllLogs();
    	}
    	return json;
    }
    
    private JsonNode getAllLogs(){
    	return null;
    }
    
    // Send a Json event to all members
    private void notifyAll(JsonNode node) {
        for(Client client: clients) {
            client.getSocket().write(node);
        }
    }
    
    private class Client {
        
        final WebSocket.Out<JsonNode> socket;
        
        public Client(WebSocket.Out<JsonNode> socket) {
            this.socket = socket;
        }
        
        public WebSocket.Out<JsonNode> getSocket() {
			return socket;
		}
        
    }
    
}
