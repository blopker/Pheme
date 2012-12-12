import java.rmi.RemoteException;

import adapters.rmi.RMI;
import play.*;

public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
    Logger.info("Application has started");
    try {
		RMI.start();
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
    RMI.stop();
  }  
    
}