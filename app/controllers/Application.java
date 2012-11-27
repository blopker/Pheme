package controllers;

import models.Logs;

import org.codehaus.jackson.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.logs;
import views.html.index;

public class Application extends Controller {
  
    /**
     * Display the start page.
     */
    public static Result index() {
    	if(JPregelRunner.isRunning()){
    		
    	}
        return ok(index.render());
    }
         
    /**
     * Display the logs.
     */
    public static Result logs() {
        return ok(logs.render());
    }
    
    /**
     * Handle the chat websocket.
     */
    public static WebSocket<JsonNode> logsSubscribe() {
        return new WebSocket<JsonNode>() {
            
            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){
                
                // Join the chat room.
                try { 
                    Logs.listen(in, out);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
  
}
