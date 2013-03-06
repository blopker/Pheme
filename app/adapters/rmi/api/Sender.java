package adapters.rmi.api;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import adapters.rmi.api.DTOs.DTO;

class Sender implements Runnable {
	final int RETRY_COUNT = 20;
	final int RETRY_DELAY = 5000;
	final String url;
	final BlockingQueue<DTO> queue;
	PhemeAPI api;

	public Sender(String url, BlockingQueue<DTO> messageQueue){
		this.url = url;
		this.queue = messageQueue;
	}
	
	private void tryConnecting(String url) {
		int retries = 0;
		while (retries <= RETRY_COUNT) {
			try {
				api = connectAPI(url);
				System.out.println("Connected to Pheme!");
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

	@Override
	public void run() {
		tryConnecting(url);
		List<DTO> messages = new ArrayList<DTO>();
		while (!Thread.currentThread().isInterrupted()) {
			// Send messages first in case there's left overs from a failure.
			try {
				api.send(messages);
				messages.clear();
				messages.add(queue.take());
				queue.drainTo(messages);
			} catch (RemoteException e) {
				System.out.println("Lost connection to Pheme! Retrying...");
				e.printStackTrace();
				tryConnecting(url);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			 
		}

	}

}