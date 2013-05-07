package rmi;

import pheme.api.Component;
import pheme.api.ComponentType;
import pheme.api.Pheme;

public class TestClient implements Runnable {
	static int COUNT_MAX = 1000;
	static int SLEEP_TIME = 100;
	static int JOB_COUNT = 3;
	static int COMPUTER_COUNT = 3;
	static String JOB_NAME_PREFIX = "TestJob";
	static String COMPUTER_NAME_PREFIX = "TestComputer";
	static String[] LOG_TYPES = { "info", "warn", "debug" };

	static Pheme pheme;
	final Component component;

	public static void main(String[] args) {
		String hostname = getHostname(args);
		pheme = new Pheme(hostname);

		// start Job components
		for (int i = 0; i < JOB_COUNT; i++) {
			new TestClient(JOB_NAME_PREFIX + i, ComponentType.JOB);

		}

		// start Computer components
		for (int i = 0; i < COMPUTER_COUNT; i++) {
			new TestClient(COMPUTER_NAME_PREFIX + i, ComponentType.COMPUTER);
		}
	}

	private static String getHostname(String[] args) {
		String hostname = "localhost";
		if (args.length >= 1) {
			hostname = args[0];
		}
		return hostname;
	}

	private TestClient(String name, ComponentType type) {
		// Register a component with Pheme.
		this.component = pheme.register(name, type);
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		for (int count = 0; count < COUNT_MAX; count++) {
			try {
				if (count % 10 == 0) {
					sendLogs(count);
				}
				sendCounts(count);
				sendGauge(count);
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(component.getComponentName() + " done!");
	}

	private void sendLogs(int count) {
		String logType = LOG_TYPES[count % LOG_TYPES.length];
		String message = "Will the real TestClient please stand up? " + count + "\n Or don't.\tSee if I care.";
		// Log a message with the component
		component.log(logType, message);
//		System.out.println(component.getComponentName() + " sent log " + count);
	}

	private void sendCounts(int count) {
		String counterName = "Test Counter " + (count % 3);
		int add = (count % 10) - 4;
		// Update component's counter
		component.count(counterName, add);
//		System.out.println(component.getComponentName() + " sent added count "
//				+ add);
	}
	
	private void sendGauge(int gauge) {
		String gaugeName = "Test Gauge " + (gauge % 3);
		int value = (gauge % 10) - 4;
		// Update component's counter
		component.gauge(gaugeName, value);
//		System.out.println(component.getComponentName() + " set guage to "
//				+ value);
	}
}
