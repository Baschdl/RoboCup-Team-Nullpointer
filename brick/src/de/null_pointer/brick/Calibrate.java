package de.null_pointer.brick;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import de.null_pointer.sensor.LightSensorArray;

public class Calibrate {
	LightSensorArray lsa = new LightSensorArray(SensorPort.S4);

	public void calibrateBlack() {

		while (!Button.ESCAPE.isDown()) {

			lsa.calibrateBlack();
			LCD.drawString("Schwarz kalibriert", 0, 0);
			Delay.msDelay(2000);
		}
	}

	public void calibrateWhite() {
		while (!Button.ESCAPE.isDown()) {
			lsa.calibrateWhite();
			LCD.drawString("Weiﬂ kalibriert", 0, 0);
			Delay.msDelay(2000);
		}
	}

}
