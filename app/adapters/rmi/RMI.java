package adapters.rmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import models.Log;

import adapters.rmi.api.PhemeAPI;

public class RMI extends UnicastRemoteObject implements PhemeAPI{
	static Registry registry;
	static PhemeAPI pheme;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void start() throws RemoteException {
	     // construct & set a security manager (unnecessary in this case)
		System.setProperty("java.security.policy", "./policy.txt");
        System.setSecurityManager(new RMISecurityManager());

        // construct an rmiregistry within this JVM using the default port
        if(registry == null){
        	registry = LocateRegistry.createRegistry(PhemeAPI.SERVICE_PORT);
        }
        
        pheme = new RMI();
        registry.rebind(PhemeAPI.SERVICE_NAME, pheme);

        System.out.println("RMI Service: Ready.");
	}
	
	public static void stop(){
		try {
			if(registry != null){
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
	}

	@Override
	public void log(String name, String type, String message)
			throws RemoteException {
		Log.create(name, type, message);		
	}

}
