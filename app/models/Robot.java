package models;

import models.datatypes.Log;

public class Robot {

	public Robot() {
		Thread t = new Thread(new RobotMouth());
		t.start();
	}

	public class RobotMouth implements Runnable {

		@Override
		public void run() {
			int count = 0;
			while (true) {
				try {
					Thread.sleep(5000);
					Log.create("Robot", "info", "Robots will destroy you " + count + " times!");
					count++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}