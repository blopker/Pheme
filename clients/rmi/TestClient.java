package rmi;

import java.util.Random;

import adapters.rmi.api.Pheme;

public class TestClient implements Runnable{
	Pheme pheme;
	
	Random r = new Random();
	
	static int COUNT_MAX = 1000;
	static int SLEEP_TIME = 100;
	static String JOB_NAME = "TestJob";
	static String COMPUTER_NAME = "TestComputer";

	public static void main(String[] args) {
		String hostname = getHostname(args);
		TestClient client = new TestClient(hostname);
		client.run();
	}
	
	private static String getHostname(String[] args) {
		String hostname = "localhost";
		if (args.length >= 1) {
			hostname = args[0];
		}
		return hostname;
	}

	private TestClient(String hostname) {
		pheme = new Pheme(hostname);
	}

	@Override
	public void run() {
		for (int count = 0; count < COUNT_MAX; count++) {
			try {
				char c = (char)(r.nextInt(10) + 'A');
				String computerName = COMPUTER_NAME + c;
				sendLogs(computerName, count);
				sendCounts(computerName, count);
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendLogs(String componentName, int count) {
		String[] logTypes = {"info", "warn", "debug"};
		pheme.log(componentName, logTypes[count % logTypes.length], "Will the real TestClient please stand up? " + count);
		System.out.println(componentName + " sent log " + count);
	}
	
	private void sendCounts(String componentName, int count) {
		pheme.count(componentName, "Test Counter", count % 10);
		System.out.println(componentName + " sent added count " + count % 10);
	}
}
