package controllers;

import models.ChatRoom;

import org.codehaus.jackson.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.chatRoom;
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
    
    public static Result stopJpregel() {
    	JPregelRunner.stopJPregel();
    	if(!JPregelRunner.isRunning()){
    		return redirect(routes.Application.index());
    	} else {
            flash("error", "jPregel is unstoppable!");
            return redirect(routes.Application.index());
    	}
	}
    
    public static Result startJpregel() {
    	JPregelRunner.startJPregel();
    	if(JPregelRunner.isRunning()){
    		return redirect(routes.Application.chatRoom("bo"));
    	} else {
            flash("error", "jPregel cannot be reached!");
            return redirect(routes.Application.index());
    	}
	}
      
    /**
     * Display the chat room.
     */
    public static Result chatRoom(final String username) {
        if(username == null || username.trim().equals("")) {
            flash("error", "Please choose a valid username.");
            return redirect(routes.Application.index());
        }
        return ok(chatRoom.render(username));
    }
    
    /**
     * Handle the chat websocket.
     */
    public static WebSocket<JsonNode> chat(final String username) {
        return new WebSocket<JsonNode>() {
            
            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){
                
                // Join the chat room.
                try { 
                    ChatRoom.join(username, in, out);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
  
}
