package pheme.api;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import pheme.api.dtos.DTO;


class Sender implements Runnable {
	final int RETRY_COUNT = 20;
	final int RETRY_DELAY = 1000;
	final String url;
	final Pheme pheme;
	PhemeAPI api;

	public Sender(String url, Pheme pheme){
		this.url = url;
		this.pheme = pheme;
	}
	
	private void tryConnecting(String url) {
		int retries = 0;
		while (retries <= RETRY_COUNT) {
			try {
				api = connectAPI(url);
				System.out.println("Connected to Pheme.");
				return;
			} catch (RemoteException e) {
				System.out.println("Cannot connect to Pheme at " + url
						+ ":" + PhemeAPI.SERVICE_PORT);
				System.out.println("Trying again in 5 seconds.");
				try {
					Thread.sleep(RETRY_DELAY);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} finally {
				retries++;
			}
		}
		System.out.println("Pheme failed to connect after 20 tries. Killing Pheme.");
		this.pheme.killClient();
	}

	/**
	 * This method takes a host name. It then sets the security policy and
	 * connects to a computer. Returned is a reference to the SpaceAPI it
	 * connected to.
	 * 
	 * @param host
	 * @return pheme
	 * @throws RemoteException
	 */
	private PhemeAPI connectAPI(String url) throws RemoteException {
		try {
			return (PhemeAPI) Naming.lookup(url);
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		throw new RemoteException();
	}

	public void run() {
		tryConnecting(url);
		List<DTO> messages = new ArrayList<DTO>();
		while (!Thread.currentThread().isInterrupted()) {
			// Check for empty message list in case there's left overs from a failure.
			try {
				if (messages.isEmpty() && pheme.drainTo(messages) == 0) {
					// Use blocking call if queue is empty.
					messages.add(pheme.take());
					pheme.drainTo(messages);
				}
				send(messages);
				messages.clear();
			} catch (RemoteException e) {
				System.out.println("Lost connection to Pheme! Retrying...");
				tryConnecting(url);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			 
		}

	}
	
	private void send(List<DTO> messages) throws RemoteException{
		int max_tries = 20;
		int tries = 0;
		// Start trying to send data
		while (tries < max_tries) {
			List<DTO> rejected = api.send(messages);
			if (rejected == null) {
				System.out.println("Sent " + messages.size() + " dataTypes.");
				return;
			}
			// Remove the successful transfers then add the rejected ones.
			messages.clear();
			messages.addAll(rejected);
			// Chill out for a sec.
			System.out.println("Server queue full, waiting...");
			try {
				Thread.sleep(RETRY_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Server refusing to accept data, killing Pheme.");
		pheme.killClient();
	}

}