package models.clients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.datatypes.DataType;
import models.datatypes.Log;

import com.google.common.eventbus.Subscribe;

import controllers.EventBus;

/**
 * A WebSocket data stream. Clients will get push updates of new datatype events
 * and can request specific datatypes.
 */
public class ClientManager {
	// Create a persistent log socket to track clients.
	static ClientManager socket = new ClientManager();
	// Client list. Client is a user of the web interface.
	private List<Client> clients = Collections
			.synchronizedList(new ArrayList<Client>());

	private ClientManager() {
		// Get in on the awesome event bus action.
		EventBus.subscribe(this);
	}

	@Subscribe
	public void dataListener(DataType data) {
		socket.notifyAll(data);
	}

	private void notifyAll(DataType data) {
		for (Client client : clients) {
			client.send(data);
		}
	}

	public static void removeClient(Client client) {
		socket.clients.remove(client);
	}
	
	public static void addClient(Client client) {
		socket.clients.add(client);
	}
}
