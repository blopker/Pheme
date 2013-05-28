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
	static Server server;

	/**
	 * Create a new Pheme client instance. This constructor will connect to the
	 * Pheme server at hostname over RMI asynchronously, so this method will not
	 * block. Data sent to this client is buffered until server communication is
	 * established. If after trying a couple of times, no connection can be made
	 * this client will silently die and drop all messages sent to it.
	 * 
	 * @param hostname
	 *            Hostname of the Pheme server.
	 */
	public Pheme(String hostname) {
		String serverDomainName = (hostname == null) ? "localhost" : hostname;
		String url = "//" + serverDomainName + "/" + PhemeAPI.SERVICE_NAME;

		sender = new Sender(url, this);
		senderThread = new Thread(sender);
		senderThread.start();
	}

	/**
	 * Register a new component with Pheme. This method constructs a new
	 * Component that can be sent the various datatypes Pheme supports.
	 * 
	 * @param componentName
	 *            Name of the component. This name will be displayed in all
	 *            references to this component. Format it for readability.
	 * @param type
	 *            ComponentType of the component to register.
	 * @return
	 */
	public Component register(String componentName, ComponentType type) {
		return new Component(componentName, type, this);
	}

	/**
	 * Convenience method for registering new Jobs.
	 * 
	 * @param componentName
	 *            Name of the job. This name will be displayed in all references
	 *            to this component. Format it for readability.
	 * @return
	 */
	public Component registerJob(String componentName) {
		return this.register(componentName, ComponentType.JOB);
	}

	/**
	 * Convenience method for registering new Computers.
	 * 
	 * @param componentName
	 *            Name of the computer. This name will be displayed in all
	 *            references to this component. Format it for readability.
	 * @return
	 */
	public Component registerComputer(String componentName) {
		return this.register(componentName, ComponentType.COMPUTER);
	}

	/**
	 * Stop the Pheme client from sending data to the server. All messages will
	 * be dropped.
	 */
	public synchronized void killClient() {
		System.out.println("Killing Pheme client.");
		if (senderThread != null) {
			senderThread.interrupt();
		}
		messageQueue.clear();
		alive = false;
	}

	/**
	 * Starts a Pheme server. This method will attempt to download the Pheme
	 * server code, extract it and run it. Returns null if the server fails to
	 * start. Using this method will construct a server that automatically shuts
	 * down when the calling JVM exits.
	 * 
	 * @return Pheme Server or null
	 */
	public static synchronized Server startServer() {
		server = Server.startServer();
		return server;
	}

	/**
	 * Starts a Pheme server. This method will attempt to download the Pheme
	 * server code, extract it and run it. Returns null if the server fails to
	 * start. Use autoShudown to specify if the server should shut down on JVM
	 * exit.
	 * 
	 * @return Pheme Server or null
	 */
	public static synchronized Server startServer(boolean autoShutdown) {
		server = Server.startServer(autoShutdown);
		return server;
	}

	/**
	 * Kills the started Pheme server. Noop if server is not up.
	 */
	public synchronized void killServer() {
		server.kill();
	}

	/**
	 * Returns the running Pheme server or null if the server is not running.
	 * 
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
