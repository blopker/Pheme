package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

import models.Log;

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
     *  Call to add a task to the compute space.
     * @param task
     * @throws RemoteException
     */
    public void log(String name, Log.Level level, String message) throws RemoteException;
}
