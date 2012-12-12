package adapters.rmi.api;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pheme {
	final PhemeAPI api;
	final BlockingQueue<Log> logQueue;
	
	public enum Level {
		INFO, DEBUG, WARN, ERROR
	}
	
	public Pheme(String hostname) {
		logQueue = new LinkedBlockingQueue<Log>();
		api = connect(hostname);
		Sender sender = new Sender();
		sender.run();
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
     */
	private PhemeAPI connect(String host) {
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
        } catch (RemoteException ex) {
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

		@Override
		public void run() {
			while(true){
				try {
					Log log = logQueue.take();
					api.log(log.getName(), log.getType(), log.getMessage());
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
