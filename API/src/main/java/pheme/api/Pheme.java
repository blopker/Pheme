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

	protected synchronized void kill() {
		System.out.println("Killing Pheme.");
		if (senderThread != null) {
			senderThread.interrupt();
		}
		messageQueue.clear();
		alive = false;
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
