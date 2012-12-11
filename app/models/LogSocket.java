package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.google.common.eventbus.Subscribe;

import controllers.EventBus;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.WebSocket;
import play.mvc.WebSocket.Out;

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
    	
    	sendAllLogs(out);
    	
    	in.onMessage(new Callback<JsonNode>() {
			
			@Override
			public void invoke(JsonNode a) throws Throwable {
				// Just echo for now.
				out.write(a);				
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
    
    
    private void sendAllLogs(Out<JsonNode> out) {
    	List<Log> logs = Log.find.all();
    	for (Log log : logs) {
    		out.write(Json.toJson(log));
		}	
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
