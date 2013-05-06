package controllers;

import models.Component;
import models.Components;
import play.Play;
import play.mvc.Controller;


/**
 * Controller to supply arguments templates needs for every page.
 * @author bo
 *
 */
public class TemplateHelper extends Controller {
    public static int getJobCount(){
    	return Component.getCount(Components.JOB);
    }
    
    public static int getComputerCount(){
    	return Component.getCount(Components.COMPUTER);
    }
    
    public static String getVersion() {
		return Play.application().configuration().getString("app.version");
	}
}
