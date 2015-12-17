package de.null_pointer.navigation.test;

import java.awt.event.ActionEvent;

import javax.swing.Timer;

import org.apache.log4j.Logger;

import de.null_pointer.navigation.map.Node;

public class MyTimer {

	private static Logger logger = Logger.getLogger(Node.class);

	private NavSimulationHandler handler = null;
	private Timer timer = null;

	/**
	 * 
	 * @param handler
	 * @param period
	 *            time between two actions
	 */
	public MyTimer(NavSimulationHandler handler, int period) {
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
		if (handler.simulate() == false) {
			timer.stop();
			logger.info("simulation has ended");
		}
	}

}
