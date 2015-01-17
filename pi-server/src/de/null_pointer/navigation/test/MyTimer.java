package de.null_pointer.navigation.test;

import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class MyTimer {

	Handler handler = null;

	Timer t = new Timer(1000, new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
			handler.simulate();
		}
	});

	public MyTimer(Handler handler) {
		this.handler = handler;
	}
	
	public void stop(){
		t.stop();
	}
	
	public void start(){
		t.start();
	}

}
