package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import models.datatypes.DataType;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import play.libs.F.Callback;
import play.mvc.WebSocket;

public class SocketClient implements Runnable {
	final WebSocket.In<JsonNode> in;
	final WebSocket.Out<JsonNode> out;
	final BlockingQueue<DataType> dataQueue;
	Thread t;

	public SocketClient(final WebSocket.In<JsonNode> in,
			final WebSocket.Out<JsonNode> out) {
		System.out.println("SocketClient connected!");
		this.in = in;
		this.out = out;
		this.dataQueue = new LinkedBlockingQueue<DataType>();

		// Make the client send from it's queue.
		t = new Thread(this);
		t.start();

		in.onMessage(new Callback<JsonNode>() {

			@Override
			public void invoke(JsonNode a) throws Throwable {
				// Just echo for now.
				out.write(a);
			}

		});
	}

	public void kill() {
		out.close();
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
				System.out.println("SocketClient disconnected!");
			}
		}

	}
}
