import java.rmi.RemoteException;

import controllers.Socket;

import adapters.rmi.RemoteService;
import play.*;
import ws.wamplay.controllers.WAMPlayServer;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		
		WAMPlayServer.addController(new Socket());
		
		try {
			RemoteService.start();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.info("Application has started");
	}

	@Override
	public void onStop(Application app) {
		Logger.info("Application shutdown...");
		RemoteService.stop();
	}

}