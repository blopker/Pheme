package pheme.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import pheme.api.dtos.DTO;



public interface PhemeAPI extends Remote{
	
	/**
     * Name of the service the client will connect to.
     */
    public static String SERVICE_NAME = "Pheme";
    
    /**
     * Port the computer will bind to.
     */
    public static int SERVICE_PORT = 1099;
    
    /**
     * Send a batch of DTOs to the server.
     * @param messages in a list to be sent.
     * @return rejected list of DTO objects. List is null if all objects were accepted.
     * @throws RemoteException
     */
    public List<DTO> send(List<DTO> messages) throws RemoteException;
}
