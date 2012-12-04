package adapters.rmi.api;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Pheme {
	final PhemeAPI api;
	
	public enum Level {
		INFO, DEBUG, WARN, ERROR
	}
	
	public Pheme(String hostname) {
		api = connect(hostname);
	}
	
	public void log(String name, String type, String message){
		try {
			api.log(name, type, message);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
}
