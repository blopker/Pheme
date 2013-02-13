package rmi;

import java.util.Random;

import adapters.rmi.api.Pheme;

public class TestClient implements Runnable{
	Pheme pheme;

	public static void main(String[] args) {
		String hostname = "localhost";
		if (args.length >= 1) {
			hostname = args[0];
		}
		TestClient client = new TestClient(hostname);
		client.run();
	}

	private TestClient(String hostname) {
		pheme = new Pheme(hostname);
	}

	@Override
	public void run() {
		int count = 0;
		Random r = new Random();
		String[] types = {"info", "warn", "debug"};
		while (count < 1000){
			try {
				char c = (char)(r.nextInt(10) + 'A');
				String myName = "TestClient" + c;
				Thread.sleep(100);
				pheme.log(myName, types[count % types.length], "Will the real TestClient please stand up?" + System.getProperty("line.separator") + count);
				System.out.println(myName + " sent log " + count);
				pheme.count(myName, "Test Counter", 1);
				System.out.println(myName + " sent added count " + 1);
				count++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
