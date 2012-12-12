package rmi;

import java.util.Random;

import adapters.rmi.api.Pheme;

public class TestClient implements Runnable{
	Pheme pheme;
	String myName;

	public static void main(String[] args) {
		TestClient client = new TestClient();
		client.run();
	}

	private TestClient() {
		Random r = new Random();
		char c = (char)(r.nextInt(26) + 'A');
		myName = "TestClient" + c;
		pheme = new Pheme("localhost");
	}

	@Override
	public void run() {
		int count = 0;
		while (true){
//			pheme.log(myName, "info", "Will the real TestClient please stand up?");
			try {
				Thread.sleep(10);
				pheme.log(myName, "info", "Will the real TestClient please stand up? " + count);
				System.out.println("sent log " + count);
				count++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
