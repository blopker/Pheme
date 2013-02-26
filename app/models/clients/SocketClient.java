package models.clients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import models.datatypes.DataType;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.WebSocket;
// Client is a user of the web interface.
public class SocketClient implements Runnable, Client {
	final WebSocket.In<JsonNode> in;
	final WebSocket.Out<JsonNode> out;
	final protected BlockingQueue<DataType> dataQueue = new LinkedBlockingQueue<DataType>();
	Thread t;
	
	// For testing
	protected SocketClient() {
		this.in = null;
		this.out = null;
	}
	
	public SocketClient(final WebSocket.In<JsonNode> in,
			final WebSocket.Out<JsonNode> out) {
		this.in = in;
		this.out = out;

		// Make the client send from it's queue.
		t = new Thread(this);
		t.start();

		in.onMessage(new Callback<JsonNode>() {

			@Override
			public void invoke(JsonNode request) throws Throwable {
//				DataType response = JsonAPI.process(request);
//				dataQueue.put(response);
			}

		});
		
		final SocketClient self = this;
		in.onClose(new Callback0() {
			@Override
			public void invoke() throws Throwable {
				// Remove client from our active client list.
				ClientManager.removeClient(self);
				self.kill();
			}
		});
	}

	public void kill() {
		out.close();
		dataQueue.clear();
		if (t.isAlive()) {
			t.interrupt();
		}
	}

	public void send(DataType dataType) {
		try {
			dataQueue.put(dataType);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sendAll(Collection<? extends DataType> datas) {
		dataQueue.addAll(datas);
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			List<DataType> dataPackage = new ArrayList<DataType>();
			try {
				dataPackage.add(dataQueue.take());
				dataQueue.drainTo(dataPackage);
				out.write(Json.toJson(dataPackage));
				dataPackage.clear();
			} catch (InterruptedException e) {
				// Client was killed.
			}
		}

	}
}
