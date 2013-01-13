package rmi;

import java.util.Random;

import adapters.rmi.api.Pheme;

public class TestClient implements Runnable{
	Pheme pheme;
	String myName;

	public static void main(String[] args) {
		String hostname = "localhost";
		if (args.length >= 1) {
			hostname = args[0];
		}
		TestClient client = new TestClient(hostname);
		client.run();
	}

	private TestClient(String hostname) {
		Random r = new Random();
		char c = (char)(r.nextInt(26) + 'A');
		myName = "TestClient" + c;
		pheme = new Pheme(hostname);
	}

	@Override
	public void run() {
		int count = 0;
		while (count < 1000){
			try {
				Thread.sleep(10);
				pheme.log(myName, "info", "Will the real TestClient please stand up? " + count);
				System.out.println("sent log " + count);
				count++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
