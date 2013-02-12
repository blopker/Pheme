package controllers;

import models.clients.ClientManager;
import models.clients.SocketClient;

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
        return ok(index.render());
    }

    /**
     * Display the logs.
     */
    public static Result logs() {
        return ok(logs.render());
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
