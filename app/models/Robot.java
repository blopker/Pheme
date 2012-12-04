package models;

public class Robot {

	public Robot() {
		Thread t = new Thread(new RobotMouth());
		t.start();
	}

	public class RobotMouth implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(5000);
					Log.create("Robot", "info", "Robots will destroy you!.");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}