package de.null_pointer.navigation.test;

import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class MyTimer {

	Handler handler = null;

	Timer timer = null;

	/**
	 * 
	 * @param handler
	 * @param period
	 *            time between two actions
	 */
	public MyTimer(Handler handler, int period) {
		this.handler = handler;
		timer = new Timer(period, new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timerAction();
			}
		});
	}

	public void stop() {
		timer.stop();
	}

	public void start() {
		timer.start();
	}

	private void timerAction() {
		handler.simulate();
	}

}
