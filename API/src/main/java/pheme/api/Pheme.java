package pheme.api;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pheme.api.dtos.DTO;

public class Pheme {
	static int QUEUE_CAPACITY = 1000;
	final BlockingQueue<DTO> messageQueue = new LinkedBlockingQueue<DTO>(
			QUEUE_CAPACITY);
	final Sender sender;
	Thread senderThread;
	boolean alive = true;
	Server server;

	public Pheme(String hostname) {
		String serverDomainName = (hostname == null) ? "localhost" : hostname;
		String url = "//" + serverDomainName + "/" + PhemeAPI.SERVICE_NAME;

		sender = new Sender(url, this);
		senderThread = new Thread(sender);
		senderThread.start();
	}

	public Component register(String component, ComponentType type) {
		return new Component(component, type, this);
	}

	public synchronized void killClient() {
		System.out.println("Killing Pheme client.");
		if (senderThread != null) {
			senderThread.interrupt();
		}
		messageQueue.clear();
		alive = false;
	}
	
	/**
	 * Starts a Pheme server. This method will attempt to download the Pheme server code, 
	 * extract it and run it. Returns null if the server fails to start.
	 * @return Pheme Server or null
	 */
	public synchronized Server startServer(){
		server = Server.startServer();
		return server;
	}
	
	public synchronized void killServer(){
		server.kill();
	}

	/**
	 * Returns the running Pheme server or null if the server is not running.
	 * @return Pheme Server or null
	 */
	public Server getServer() {
		return server;
	}
	
	protected synchronized boolean send(DTO dto) {
		if (alive) {
			return messageQueue.offer(dto);
		}
		return false;
	}

	protected DTO take() throws InterruptedException {
		return messageQueue.take();
	}

	protected synchronized int drainTo(List<DTO> dtoList) {
		return messageQueue.drainTo(dtoList);
	}
}
