package de.null_pointer.motorcontrol_pi;

import lejos.robotics.subsumption.Arbitrator;

public class Semaphore {
	private Arbitrator arbitrator = null;
	private final Object lock = new Object();

	private int blockedMotors = 0;

	public Semaphore(Arbitrator arbitrator) {
		this.arbitrator = arbitrator;
	}

	public void up() {
		synchronized (lock) {
			blockedMotors++;
			if (blockedMotors == 1) {
				try {
					arbitrator.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void down() {
		synchronized (lock) {
			blockedMotors--;
			if (blockedMotors == 0) {
				arbitrator.notify();
			}
		}
	}

}
