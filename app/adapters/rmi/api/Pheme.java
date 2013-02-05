package adapters.rmi.api;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import adapters.rmi.api.MessageRMI;

public class Pheme {
	final BlockingQueue<MessageRMI> messageQueue;

	public Pheme(String hostname) {
		messageQueue = new LinkedBlockingQueue<MessageRMI>();
		Sender sender = new Sender(hostname);
		Thread t = new Thread(sender);
		t.start();
	}
	
	public void log(String name, String type, String message){
		try {
			messageQueue.put(new LogDTO(name, type, message));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void send(MessageRMI m){
		try {
			messageQueue.put(m);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	 /**
     * This method takes a host name. It then sets the security policy 
     * and connects to a computer. Returned is a reference to the SpaceAPI
     * it connected to.
     * 
     * @param host
     * @return pheme or null
	 * @throws RemoteException 
     */
	private PhemeAPI connect(String host) throws RemoteException {
        String serverDomainName = (host == null) ? "localhost" : host;
        String url = "//" + serverDomainName + "/" + PhemeAPI.SERVICE_NAME;

        // Can throw exceptions - see API for java.rmi.Naming.lookup
        PhemeAPI pheme;
        try {
        	pheme = (PhemeAPI) Naming.lookup(url);
        	return pheme;
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
        	ex.printStackTrace();
        }
        
        System.exit(1);
        return null;
    }
	
	
	private class Sender implements Runnable{
		final String hostname;
		PhemeAPI api;

		public Sender(String hostname) {
			this.hostname = hostname;
		}
		
		private void connectAPI() {
			boolean connected = false;
			while(!connected){
				try {
					api = connect(hostname);
					connected = true;
					System.out.println("Connected to Pheme!");
				} catch (RemoteException e) {
					System.out.println("Cannot connect to Pheme at " + hostname + ":" + PhemeAPI.SERVICE_PORT); 
					System.out.println("Trying again in 5 seconds.");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		@Override
		public void run() {
			connectAPI();
			List<MessageRMI> messages = new ArrayList<MessageRMI>();
			while(true){
				try {
					messages.add(messageQueue.take());
					messageQueue.drainTo(messages);
					api.send(messages);
					messages.clear();
				} catch (RemoteException e) {
					e.printStackTrace();
					connectAPI();
				} catch (InterruptedException e) {
					e.printStackTrace();
					connectAPI();
				}
			}
			
		}
		
	}
}
