package de.null_pointer.brick;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;
import lejos.util.TextMenu;
import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.communication_brick.InitCommunicationBrick;

public class Brick {

	private static String[] mainMenuStrings = { "connect", "sensors",
			"calibrate", "exit" };
	private static String[] sensorMenuStrings = { "DistNX", "EOPD",
			"LSA", "AbsIMU", "Thermal", "return" };
	private static String[] calibrationMenuStrings = { "LSA white",
			"LSA black", "return" };
	private static String[] absIMUMenuStrings = { "TiltData", "Gyro", "return"};

	private static TextMenu mainMenu = new TextMenu(mainMenuStrings);
	private static TextMenu sensorMenu = new TextMenu(sensorMenuStrings);
	private static TextMenu calibrationMenu = new TextMenu(
			calibrationMenuStrings);
	private static TextMenu absIMUMenu = new TextMenu(absIMUMenuStrings);
	private static AllValues allVal = new AllValues();
	private static Calibrate calibration = new Calibrate();

	public static void main(String[] args) {
		while (true) {
			mainMenu();
		}
	}

	private static void mainMenu() {
		switch (mainMenu.select()) {
		case 0:
			LCD.clear();
			connect();
			break;
		case 1:
			LCD.clear();
			showSensors();
			break;
		case 2:
			LCD.clear();
			calibrateSensors();
			break;
		case 3:
			System.exit(1);
			break;
		}
	}

	private static void connect() {
		try {
			System.out.println("Starting communication... ");
			InitCommunicationBrick initCom = new InitCommunicationBrick();
			BrickControlBrick comBrick = new BrickControlBrick(
					initCom.initConnection());
			System.out.println("Communication started.");

			comBrick.start();
			comBrick.setDaemon(true);
			System.out.println("Preparations finished.");
			while (!Button.ESCAPE.isDown()) {
				Delay.msDelay(500);
			}
		} catch (Exception e) {
			System.out.println("A error occured!");
		}
	}

	private static void showSensors() {
		switch (sensorMenu.select()) {
		case 0:
			LCD.clear();
			allVal.showDistNX();
			break;
		case 1:
			LCD.clear();
			allVal.showEOPD();
			break;
		case 2:
			LCD.clear();
			allVal.showValuesLSA();
			break;
		case 3:
			LCD.clear();
			showAbsIMU();
			//allVal.showAbsIMU();
			break;
		case 4:
			LCD.clear();
			allVal.showThermal();
			break;
		case 5:
			// return
			LCD.clear();
			break;
		}
	}

	private static void calibrateSensors() {
		switch (calibrationMenu.select()) {
		case 0:
			LCD.clear();
			calibration.calibrateWhite();
			break;
		case 1:
			LCD.clear();
			calibration.calibrateBlack();
			break;
		case 2:
			// return
			LCD.clear();
			break;
		}

	}
	
	private static void showAbsIMU(){
		switch (absIMUMenu.select()){
		case 0:
			LCD.clear();
			allVal.showAbsIMUTiltData();
			break;
		case 1:
			LCD.clear();
			allVal.showAbsIMUGyro();
			break;
		case 2:
			//return
			LCD.clear();
			break;
		}

	}
}