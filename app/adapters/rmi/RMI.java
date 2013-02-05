package adapters.rmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import models.Log;

import adapters.rmi.api.LogDTO;
import adapters.rmi.api.MessageRMI;
import adapters.rmi.api.PhemeAPI;

public class RMI extends UnicastRemoteObject implements PhemeAPI {
	static Registry registry;
	static PhemeAPI pheme;
	final BlockingQueue<MessageRMI> messageQueue;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void start() throws RemoteException {
		// construct an rmiregistry within this JVM using the default port
		if (registry == null) {
			registry = LocateRegistry.createRegistry(PhemeAPI.SERVICE_PORT);
		}

		pheme = new RMI();
		registry.rebind(PhemeAPI.SERVICE_NAME, pheme);

		System.out.println("RMI Service: Ready.");
	}

	public static void stop() {
		try {
			if (registry != null) {
				registry.unbind(PhemeAPI.SERVICE_NAME);
				UnicastRemoteObject.unexportObject(pheme, true);
				UnicastRemoteObject.unexportObject(registry, true);
				System.out.println("RMI Service: Shutdown.");
			}
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private RMI() throws RemoteException {
		super();
		messageQueue = new LinkedBlockingQueue<MessageRMI>();
		MessageProcessor p = new MessageProcessor();
		Thread t = new Thread(p);
		t.start();
	}

	@Override
	public void send(List<MessageRMI> messages) throws RemoteException {
			messageQueue.addAll(messages);
			System.out.println("Got " + messages.size() + " messages.");
	}

	private class MessageProcessor implements Runnable {

		@Override
		public void run() {
			while (true) {
				MessageRMI m;
				try {
					m = messageQueue.take();
					if (m instanceof LogDTO) {
						LogDTO log = (LogDTO) m;
						Log.create(log.getClient(), log.getType(),
								log.getMessage());
//						System.out.println(log.getMessage());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
