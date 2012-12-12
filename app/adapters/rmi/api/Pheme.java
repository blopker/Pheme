package adapters.rmi.api;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pheme {
	final BlockingQueue<Log> logQueue;
	
	public Pheme(String hostname) {
		logQueue = new LinkedBlockingQueue<Log>();
		Sender sender = new Sender(hostname);
		Thread t = new Thread(sender);
		t.start();
	}
	
	public void log(String name, String type, String message){
		try {
			logQueue.put(new Log(name, type, message));
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
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
        System.setSecurityManager(new RMISecurityManager());

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
	
	private class Log{
		final String name;
		final String type;
		final String message;
		
		public Log(String name, String type, String message) {
			this.name = name;
			this.type = type;
			this.message = message;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public String getMessage() {
			return message;
		}

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
			
			while(true){
				try {
					Log log = logQueue.take();
					api.log(log.getName(), log.getType(), log.getMessage());
				} catch (RemoteException e) {
					connectAPI();
				} catch (InterruptedException e) {
					connectAPI();
				}
			}
			
		}
		
	}
}
