package rmi;

import adapters.rmi.api.Pheme;

public class TestClient implements Runnable{
	Pheme pheme;
	String myName;

	public static void main(String[] args) {
		TestClient client = new TestClient();
		client.run();
	}

	private TestClient() {
		myName = "TestClient" + System.currentTimeMillis();
		pheme = new Pheme("localhost");
	}

	@Override
	public void run() {
		while (true){
			try {
				Thread.sleep(2000);
				pheme.log(myName, "info", "Will the real TestClient please stand up?");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
