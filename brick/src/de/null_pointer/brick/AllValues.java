package de.null_pointer.brick;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.EOPD;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.util.Delay;
import de.null_pointer.sensor.DThermalIR;
import de.null_pointer.sensor.LightSensorArray;

public class AllValues {

	private OpticalDistanceSensor distNX = new OpticalDistanceSensor(
			SensorPort.S3);
	private EOPD eopd = new EOPD(SensorPort.S2);
	private LightSensorArray lsa = new LightSensorArray(SensorPort.S4);
	private int[] lsavalues = new int[8];
	private  DThermalIR thermalsensor = new DThermalIR(SensorPort.S1);

	public AllValues() {

	}

	public void showDistNX() {
		try {
			while (!Button.ESCAPE.isDown()) {
				LCD.drawInt(distNX.getDistance(), 0, 0);
				Delay.msDelay(100);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void showEOPD() {
		try {
			while (!Button.ESCAPE.isDown()) {
				LCD.drawInt(eopd.readRawValue(), 0, 1);
				Delay.msDelay(100);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void showValuesLSA() {

		try {
			while (!Button.ESCAPE.isDown()) {
				lsavalues = lsa.getLightValues();
				LCD.drawString(lsavalues[0] + ";", 0, 0);
				LCD.drawString(lsavalues[1] + ";", 4, 0);
				LCD.drawString(lsavalues[2] + ";", 8, 0);
				LCD.drawString(lsavalues[3] + ";", 0, 1);
				LCD.drawString(lsavalues[4] + ";", 4, 1);
				LCD.drawString(lsavalues[5] + ";", 8, 1);
				LCD.drawString(lsavalues[6] + ";", 0, 2);
				LCD.drawString(lsavalues[7] + ";", 4, 2);
				Delay.msDelay(100);
				LCD.clear();
			}
		} catch (Exception e) {

		}
	}

	public void showAbsIMU() {
		// TODO Insert Code for showing AbsIMU on the LCD
	}

	public void showThermal() { //TODO Choose SensorPort for thermalsensor (e.g. 0 on Brick 1)
		try {
			while (!Button.ESCAPE.isDown()) {
				LCD.drawString(Float.toString(thermalsensor.readObject()), 0, 1);
				Delay.msDelay(100);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
