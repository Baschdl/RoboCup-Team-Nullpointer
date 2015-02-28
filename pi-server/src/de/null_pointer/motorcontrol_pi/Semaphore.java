package de.null_pointer.motorcontrol_pi;

import lejos.robotics.subsumption.Arbitrator;

public class Semaphore {
	private Arbitrator arbitrator = null;

	int isBlocked = 0;

	public Semaphore(Arbitrator arbitrator) {
		this.arbitrator = arbitrator;
	}

	public synchronized void up() {
		isBlocked++;
		if (isBlocked == 1) {
			try {
				arbitrator.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public synchronized void down() {
		isBlocked--;
		if (isBlocked == 0) {
			arbitrator.notify();
		}
	}

}
