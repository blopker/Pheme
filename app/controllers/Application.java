package controllers;

import java.util.List;

import models.Component;
import models.Components;
import models.datatypes.Log;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.component;
import views.html.componentsList;
import views.html.index;
import views.html.logs;

public class Application extends Controller {

	/**
	 * Display the start page.
	 */
	public static Result index() {
		return ok(index.render());
	}

	/**
	 * Display all the logs.
	 */
	public static Result logs() {
		List<Log> logList = Log.getAll();
		return ok(logs.render(logList));
	}

	/**
	 * Get a list of all the running jobs
	 */
	public static Result jobList() {
		List<Component> jobs = Component.getAll(Components.JOB);
		return ok(componentsList.render(jobs, "Jobs"));
	}

	/**
	 * Inspect a specific job
	 */
	public static Result job(String id) {
		Component job = Component.get(id);
		if (job == null) {
			return redirect(controllers.routes.Application.jobList());
		}
		return ok(component.render(job));
	}

	/**
	 * Get a list of all the running computers
	 */
	public static Result computerList() {
		List<Component> computers = Component.getAll(Components.COMPUTER);
		return ok(componentsList.render(computers, "Computers"));
	}

	/**
	 * Inspect a specific computer
	 */
	public static Result computer(String id) {
		Component computer = Component.get(id);
		if (computer == null) {
			return redirect(controllers.routes.Application.computerList());
		}
		return ok(component.render(computer));
	}
}
