package de.null_pointer.brick;

import lejos.nxt.Button;
import lejos.util.Delay;
import lejos.util.TextMenu;
import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.communication_brick.InitCommunicationBrick;

public class Brick {

	private static String[] mainMenuStrings = { "connect", "sensors",
			"calibrate", "exit" };
	private static String[] sensorMenuStrings = { "DistNX", "EOPD right",
			"EOPD left", "LSA", "'AbsIMU", "Thermal", "return" };
	private static String[] calibrationMenuStrings = { "LSA white",
			"LSA black", "return" };

	private static TextMenu mainMenu = new TextMenu(mainMenuStrings);
	private static TextMenu sensorMenu = new TextMenu(sensorMenuStrings);
	private static TextMenu calibrationMenu = new TextMenu(
			calibrationMenuStrings);

	public static void main(String[] args) {
		mainMenu();
	}

	private static void mainMenu() {
		switch (mainMenu.select()) {
		case 0:
			connect();
			break;
		case 1:
			showSensors();
			break;
		case 2:
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
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			// return
			mainMenu();
			break;
		}
	}

	private static void calibrateSensors() {
		switch (calibrationMenu.select()) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			// return
			mainMenu();
			break;
		}

	}
}