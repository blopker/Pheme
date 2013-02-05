package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.codehaus.jackson.JsonNode;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.WebSocket;
import play.mvc.WebSocket.Out;

import com.google.common.eventbus.Subscribe;

import controllers.EventBus;

/**
 * A WebSocket data stream. Clients will get push updates of new datatype events and can
 * request specific datatypes.
 */
public class Socket {
	// Create a persistent log socket to track clients.
	static Socket logSocket = new Socket();
	final BlockingQueue<Log> logQueue;
	// Client list. Client is a user of the web interface.
	private List<Client> clients = Collections
			.synchronizedList(new ArrayList<Client>());

	public Socket() {
		logQueue = new LinkedBlockingQueue<Log>();

		// Create a sender to read form the log queue.
		LogSender logSender = new LogSender();
		Thread t = new Thread(logSender);
		t.start();

		// Get in on the awesome event bus action.
		EventBus.subscribe(this);
	}

	/**
	 * Connect/register a client to the current Socket instance.
	 */
	public static void connect(WebSocket.In<JsonNode> in,
			final WebSocket.Out<JsonNode> out) throws Exception {
		logSocket.connectImpl(in, out);
	}

	private void connectImpl(WebSocket.In<JsonNode> in,
			final WebSocket.Out<JsonNode> out) {
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

	@Subscribe
	public void logListener(Log log) {
		try {
			logQueue.put(log);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendAllLogs(Out<JsonNode> out) {
		List<Log> logs = Log.find.all();
		logQueue.addAll(logs);
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

	private class LogSender implements Runnable {

		// Send a Json event to all members
		private void notifyAll(JsonNode node) {
			for (Client client : clients) {
				client.getSocket().write(node);
			}
		}

		@Override
		public void run() {
			while (true) {
				List<Log> logs = new ArrayList<Log>();
				while (true) {
					try {
						logs.add(logQueue.take());
						logQueue.drainTo(logs);
						notifyAll(Json.toJson(logs));
						logs.clear();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
