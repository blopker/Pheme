package adapters.rmi;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import models.Log;

import adapters.rmi.api.PhemeAPI;

public class RMI extends UnicastRemoteObject implements PhemeAPI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void start() throws RemoteException {
	     // construct & set a security manager (unnecessary in this case)
        System.setSecurityManager(new RMISecurityManager());

        // construct an rmiregistry within this JVM using the default port
        Registry registry = LocateRegistry.createRegistry(PhemeAPI.SERVICE_PORT);

        final PhemeAPI pheme = new RMI();

        registry.rebind(PhemeAPI.SERVICE_NAME, pheme);

        System.out.println("RMI Service: Ready.");
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
