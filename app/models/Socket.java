package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.datatypes.DataType;
import models.datatypes.Log;

import org.codehaus.jackson.JsonNode;

import play.libs.F.Callback0;
import play.mvc.WebSocket;

import com.google.common.eventbus.Subscribe;

import controllers.EventBus;

/**
 * A WebSocket data stream. Clients will get push updates of new datatype events
 * and can request specific datatypes.
 */
public class Socket {
	// Create a persistent log socket to track clients.
	static Socket socket = new Socket();
	// Client list. Client is a user of the web interface.
	private List<SocketClient> clients = Collections
			.synchronizedList(new ArrayList<SocketClient>());

	public Socket() {
		// Get in on the awesome event bus action.
		EventBus.subscribe(this);
	}

	/**
	 * Connect/register a client to the current Socket instance.
	 */
	public static void connect(WebSocket.In<JsonNode> in,
			final WebSocket.Out<JsonNode> out) throws Exception {
		socket.connectImpl(in, out);
	}

	private void connectImpl(WebSocket.In<JsonNode> in,
			final WebSocket.Out<JsonNode> out) {
		final SocketClient client = new SocketClient(in, out);
		clients.add(client);

		sendAllLogs(client);

		in.onClose(new Callback0() {
			@Override
			public void invoke() throws Throwable {
				// Remove client from our active client list.
				client.kill();
				clients.remove(client);
			}
		});
	}

	@Subscribe
	public void dataListener(DataType data) {
		notifyAll(data);
	}

	private void sendAllLogs(SocketClient client) {
		List<Log> logs = Log.find.all();
		client.sendAll(logs);
	}

	private void notifyAll(DataType data) {
		for (SocketClient client : clients) {
			client.send(data);
		}
	}
}
