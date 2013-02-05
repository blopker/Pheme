package adapters.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface PhemeAPI extends Remote{
	/**
     * Name of the service the client will connect to.
     */
    public static String SERVICE_NAME = "Pheme";
    /**
     * Port the computer will bind to.
     */
    public static int SERVICE_PORT = 1099;
    
    public void send(List<MessageRMI> messages) throws RemoteException;
}
