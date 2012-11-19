package controllers;

public class JPregelRunner {
	private static boolean running = false;
	
	public static boolean startJPregel() {
		running = true;
		return running;
	}
	
	public static boolean stopJPregel() {
		running = false;
		return running;
	}
	
	public static boolean isRunning() {
		return running;
	}
}
