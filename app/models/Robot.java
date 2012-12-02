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
					Thread.sleep(1000);
					Log.create("Robot", Log.Level.INFO, "Robots are sexy.");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}