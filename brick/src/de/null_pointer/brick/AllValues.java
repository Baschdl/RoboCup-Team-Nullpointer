package de.null_pointer.brick;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.EOPD;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.util.Delay;
import de.null_pointer.sensor.AbsoluteIMU_ACG;
import de.null_pointer.sensor.DThermalIR;
import de.null_pointer.sensor.LightSensorArray;

public class AllValues {

	private OpticalDistanceSensor distNX;
	private EOPD eopd;
	private LightSensorArray lsa;
	private int[] lsavalues = new int[8];
	private  DThermalIR thermalsensor;
	private AbsoluteIMU_ACG absimu;

	public AllValues() {

	}

	public void showDistNX() {
		distNX = new OpticalDistanceSensor(
				SensorPort.S3);
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
		eopd = new EOPD(SensorPort.S2);
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
		lsa = new LightSensorArray(SensorPort.S4);

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
			System.out.println(e);
		}
	}

	public void showAbsIMU() {
		absimu = new AbsoluteIMU_ACG(SensorPort.S1);
		try{
			while(!Button.ESCAPE.isDown()){
				LCD.drawString(Integer.toString(absimu.getTiltData()[0]), 0, 0);
				LCD.drawString(Integer.toString(absimu.getTiltData()[1]), 0, 1);
				LCD.drawString(Integer.toString(absimu.getTiltData()[2]), 0, 2);
				Delay.msDelay(100);
				LCD.clear();
			}
		} catch (Exception e){
			System.out.println(e);
		}
	}

	public void showThermal() {
		thermalsensor = new DThermalIR(SensorPort.S3);
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
