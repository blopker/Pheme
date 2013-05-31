package rmi;

import pheme.api.Component;
import pheme.api.ComponentType;
import pheme.api.Pheme;

public class PerformanceClient implements Runnable {
	static int TEST_WAIT_SEC = 3;
	static int TEST_RUN_SEC = 10;
	static int START_DELAY_MSEC = 50;
	static int DELAY_DECAY_MSEC = 5;

	static String JOB_NAME_PREFIX = "Performance Test";

	static Pheme pheme;
	private Component component;

	public static void main(String[] args) {
		String hostname = getHostname(args);
		pheme = new Pheme(hostname);

		// Start test
		new PerformanceClient(JOB_NAME_PREFIX, ComponentType.JOB);

	}

	private static String getHostname(String[] args) {
		String hostname = "localhost";
		if (args.length >= 1) {
			hostname = args[0];
		}
		return hostname;
	}

	private PerformanceClient(String name, ComponentType type) {
		// Register a component with Pheme.
		this.component = pheme.register(name, type);
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
        try {
            // Wait a couple for the client to connect...
            Thread.sleep(3000);
        } catch (Exception e) {
        }
		for (int delay = START_DELAY_MSEC; delay > 0; delay -= DELAY_DECAY_MSEC) {
            System.out.println("Starting test with delay " + delay);
			runTest(delay);
			try {
				Thread.sleep(TEST_WAIT_SEC * 1000);
			} catch (Exception e) {
			}
		}
		System.out.println(component.getComponentName() + " done!");
	}

	private void runTest(int delay) {
		this.component.log("INFO", "Starting test with delay of " + delay);
		long total_count = TEST_RUN_SEC * 1000 / delay;
		for (int count = 0; count < total_count; count++) {
		    component.count("Performance Count " + delay, 1);
			try {
				Thread.sleep(delay);
			} catch (Exception e) {
			}
		}
		this.component.log("INFO", "Finished test with delay of " + delay);
	}
}
