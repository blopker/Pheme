package adapters.rmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pheme.api.PhemeAPI;
import pheme.api.dtos.DTO;
import play.Logger;
import play.Logger.ALogger;

public class RemoteService extends UnicastRemoteObject implements PhemeAPI {
	private static final long serialVersionUID = -551760291730129249L;
	static Registry registry;
	static PhemeAPI pheme;
	final BlockingQueue<DTO> messageQueue;
	static ALogger log = Logger.of(RemoteService.class.getSimpleName());
	
	public static void start() throws RemoteException {
		// construct an rmiregistry within this JVM using the default port
		if (registry == null) {
			registry = LocateRegistry.createRegistry(PhemeAPI.SERVICE_PORT);
		}

		pheme = new RemoteService();
		registry.rebind(PhemeAPI.SERVICE_NAME, pheme);

		log.info("RMI Service: Ready.");
	}

	public static void stop() {
		try {
			if (registry != null) {
				registry.unbind(PhemeAPI.SERVICE_NAME);
				UnicastRemoteObject.unexportObject(pheme, true);
				UnicastRemoteObject.unexportObject(registry, true);
				log.info("RMI Service: Shutdown.");
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

	private RemoteService() throws RemoteException {
		super();
		messageQueue = new LinkedBlockingQueue<DTO>();
		MessageProcessor p = new MessageProcessor();
		Thread t = new Thread(p);
		t.start();
	}

	@Override
	public void send(List<DTO> dto) throws RemoteException {
			messageQueue.addAll(dto);
			log.info("Got " + dto.size() + " data types.");
	}

	private class MessageProcessor implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					DTOMapper.create(messageQueue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
