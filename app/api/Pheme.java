package api;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import models.Log;

public class Pheme {
	final PhemeAPI api;
	
	public Pheme(String hostname, String compententName) {
		api = connect(hostname);
	}
	
	public void log(String name, Log.Level level, String message) throws RemoteException{
		api.log(name, level, message);
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
