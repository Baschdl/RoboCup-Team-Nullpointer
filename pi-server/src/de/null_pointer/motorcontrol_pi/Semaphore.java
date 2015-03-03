package de.null_pointer.motorcontrol_pi;

import de.null_pointer.behavior.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Semaphore {
	private Arbitrator arbitrator = null;
	private Behavior[] behaviors = null;
	private final Object lock = new Object();

	private int blockedMotors = 0;

	public Semaphore(Arbitrator arbitrator, Behavior[] behaviors) {
		this.arbitrator = arbitrator;
		this.behaviors = behaviors;
	}

	public void up() {
		synchronized (lock) {
			blockedMotors++;
			if (blockedMotors == 1) {
				arbitrator.stopArbitrator();
			}
		}

	}

	public void down() {
		synchronized (lock) {
			blockedMotors--;
			if (blockedMotors == 0) {
				arbitrator = new Arbitrator(behaviors);
				arbitrator.setDaemon(true);
				arbitrator.start();
			}
		}
	}

}
