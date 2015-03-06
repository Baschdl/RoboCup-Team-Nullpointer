package de.null_pointer.communication_brick;

import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import de.null_pointer.sensorprocessing_brick.Abs_ImuProcessingBrick;
import de.null_pointer.sensorprocessing_brick.AccumulatorProcessingBrick;
import de.null_pointer.sensorprocessing_brick.DistNxProcessingBrick;
import de.null_pointer.sensorprocessing_brick.EOPDProcessingBrick;
import de.null_pointer.sensorprocessing_brick.LSAProcessingBrick;
import de.null_pointer.sensorprocessing_brick.SensorProcessingThread;
import de.null_pointer.sensorprocessing_brick.ThermalSensorProcessingBrick;

public class BrickControlBrick extends Thread {

	private Abs_ImuProcessingBrick abs_imu = null;
	private AccumulatorProcessingBrick accumulator = null;
	private DistNxProcessingBrick distnx = null;
	private EOPDProcessingBrick leftEOPD = null;
	private EOPDProcessingBrick rightEOPD = null;
	private LSAProcessingBrick lsa = null;
	private ThermalSensorProcessingBrick thermal = null;
	private ColorSensor colorS = null;

	SensorProcessingThread sensorProcessing = null;

	private int linearAcceleration = 6000;
	private int rotationAcceleration = 6000;
	private int rotationSpeed = 50;

	CommunicationBrick com = null;

	public BrickControlBrick(CommunicationBrick communication) {
		this.com = communication;
	}

	public void run() {
		int i = 0;
		while (true) {
			if (i < 3) {
				this.receiveCommand();
				i++;
			} else if (i == 3) {
				this.receiveCommand();
				sensorProcessing = new SensorProcessingThread(this, abs_imu,
						accumulator, distnx, leftEOPD, rightEOPD, lsa, thermal);
				sensorProcessing.start();
				sensorProcessing.setDaemon(true);
				i++;
			} else {
				this.receiveCommand();
			}
		}
	}

	public void receiveCommand() {
		int[] command = new int[4];

		// System.out.println("Lesen...");
		String commandString = com.receiveString();
		// System.out.println("fertig!");

		int indexMaxDataString = commandString.length() - 1;
		if (commandString.charAt(0) == '*'
				&& commandString.charAt(indexMaxDataString) == '#') {
			commandString = commandString.substring(1, indexMaxDataString);
			String puf[] = new String[4];
			int index1 = 0, index2 = 0;
			for (int i = 0; i < 4; i++) {
				if (index1 < commandString.length()) {
					index2 = commandString.indexOf(";", index1);
					if (index2 >= 0) {
						puf[i] = commandString.substring(index1, index2);
						index1 = index2 + 1;
					} else {
						puf[i] = commandString.substring(index1);
					}
				}
				command[i] = Integer.parseInt(puf[i]);
			}
			processCommand(command);
		} else {
			System.err.println("Fehlerhafte Daten");
		}

	}

	private void processCommand(int[] command) {
		switch (command[0]) {
		case 1:
			switch (command[1]) {
			case 1:
				distnx.setPower(true);
				break;
			case 2:
				distnx.setPower(false);
				break;
			case 5:
				sendData(1, 5, distnx.getSensorModule());
				break;
			default:
				break;
			}
			break;
		case 2:
			switch (command[1]) {
			case 1:
				sendData(2, 10, lsa.sleep(false));
				break;
			case 2:
				sendData(2, 11, lsa.sleep(true));
				break;
			case 3:
				sendData(2, 12, lsa.calibrateBlack());
				break;
			case 4:
				sendData(2, 13, lsa.calibrateWhite());
				break;
			default:
				break;
			}
			break;
		case 3:
			switch (command[1]) {
			case 1:
				leftEOPD.setLongRange(false);
				break;
			case 2:
				leftEOPD.setLongRange(true);
				break;
			}
			break;
		case 4:
			switch (command[1]) {
			case 1:
				rightEOPD.setLongRange(false);
				break;
			case 2:
				rightEOPD.setLongRange(true);
				break;
			}
			break;
		case 5:
			switch (command[1]) {
			case 6:
				sendData(5, 14, abs_imu.getFilter());
				break;
			case 7:
				sendData(5, 15, abs_imu.setFilter(command[2]));
				break;
			}
			break;
		case 9:
			System.out.println("Motor" + command[1] + ";" + command[2] + ";"
					+ command[3]);
			switch (command[1]) {
			case 1:
				if (command[2] != 0 && command[3] != 5) {
					Motor.A.setSpeed(command[2]);
				}
				switch (command[3]) {
				case 1:
					// forward
					if (Motor.A.getAcceleration() != linearAcceleration) {
						Motor.A.setAcceleration(linearAcceleration);
					}
					Motor.A.forward();
					break;
				case 2:
					// forward
					if (Motor.A.getAcceleration() != linearAcceleration) {
						Motor.A.setAcceleration(linearAcceleration);
					}
					Motor.A.backward();
					break;
				case 3:
					// stop
					Motor.A.stop();
					break;
				case 4:
					// float
					Motor.A.flt();
					break;
				case 5:
					// rotate
					if (Motor.A.getAcceleration() != rotationAcceleration) {
						Motor.A.setAcceleration(rotationAcceleration);
					}
					if (Motor.A.getSpeed() != rotationSpeed) {
						Motor.A.setSpeed(rotationSpeed);
					}
					Motor.A.rotate(command[2], true);
					while (Motor.A.isMoving()) {
					}
					;
					sendData(10, 1, 0);
					break;
				case 6:
					Motor.A.resetTachoCount();
					break;
				default:
					break;
				}

				break;
			case 2:
				if (command[2] != 0 && command[3] != 5) {
					Motor.B.setSpeed(command[2]);
				}
				switch (command[3]) {
				case 1:
					// forward
					if (Motor.B.getAcceleration() != linearAcceleration) {
						Motor.B.setAcceleration(linearAcceleration);
					}
					Motor.B.forward();
					break;
				case 2:
					// backward
					if (Motor.B.getAcceleration() != linearAcceleration) {
						Motor.B.setAcceleration(linearAcceleration);
					}
					Motor.B.backward();
					break;
				case 3:
					// stop
					Motor.B.stop();
					break;
				case 4:
					// float
					Motor.B.flt();
					break;
				case 5:
					// rotate
					if (Motor.B.getAcceleration() != rotationAcceleration) {
						Motor.B.setAcceleration(rotationAcceleration);
					}
					if (Motor.B.getSpeed() != rotationSpeed) {
						Motor.B.setSpeed(rotationSpeed);
					}
					Motor.B.rotate(command[2], true);
					while (Motor.B.isMoving()) {
					}
					;
					sendData(11, 1, 0);
					break;
				case 6:
					Motor.B.resetTachoCount();
					break;
				default:
					break;
				}
				break;
			case 3:
				if (command[2] != 0 && command[3] != 5) {
					Motor.C.setSpeed(command[2]);
				}
				switch (command[3]) {
				case 1:
					// forward
					if (Motor.C.getAcceleration() != linearAcceleration) {
						Motor.C.setAcceleration(linearAcceleration);
					}
					Motor.C.forward();
					break;
				case 2:
					// backward
					if (Motor.C.getAcceleration() != linearAcceleration) {
						Motor.C.setAcceleration(linearAcceleration);
					}
					Motor.C.backward();
					break;
				case 3:
					// stop
					Motor.C.stop();
					break;
				case 4:
					// float
					Motor.C.flt();
					break;
				case 5:
					// rotate
					if (Motor.C.getAcceleration() != rotationAcceleration) {
						Motor.C.setAcceleration(rotationAcceleration);
					}
					if (Motor.C.getSpeed() != rotationSpeed) {
						Motor.C.setSpeed(rotationSpeed);
					}
					Motor.C.rotate(command[2], true);
					while (Motor.C.isMoving()) {
					}
					;
					sendData(12, 1, 0);
					break;
				case 6:
					Motor.C.resetTachoCount();
					break;
				default:
					break;
				}
				break;
			case 4:
				if (command[2] != 0 && (command[3] != 5 || command[3] != 6)) {
					Motor.A.setSpeed(command[2]);
					Motor.B.setSpeed(command[2]);
				}

				switch (command[3]) {
				case 1:
					// forward
					if (Motor.A.getAcceleration() != linearAcceleration
							|| Motor.B.getAcceleration() != linearAcceleration) {
						Motor.A.setAcceleration(linearAcceleration);
						Motor.B.setAcceleration(linearAcceleration);
					}
					Motor.A.forward();
					Motor.B.backward();
					break;
				case 2:
					// backward
					if (Motor.A.getAcceleration() != linearAcceleration
							|| Motor.B.getAcceleration() != linearAcceleration) {
						Motor.A.setAcceleration(linearAcceleration);
						Motor.B.setAcceleration(linearAcceleration);
					}
					Motor.A.backward();
					Motor.B.forward();
					break;
				case 3:
					// stop
					Motor.A.stop();
					Motor.B.stop();
					break;
				case 4:
					// float
					Motor.A.flt();
					Motor.B.flt();
					break;
				case 5:
					// rotate
					if (Motor.A.getAcceleration() != rotationAcceleration
							|| Motor.B.getAcceleration() != rotationAcceleration) {
						Motor.A.setAcceleration(rotationAcceleration);
						Motor.B.setAcceleration(rotationAcceleration);
					}
					if (Motor.A.getSpeed() != rotationSpeed
							|| Motor.B.getSpeed() != rotationSpeed) {
						Motor.A.setSpeed(rotationSpeed);
						Motor.B.setSpeed(rotationSpeed);
					}
					Motor.A.rotate(command[2], true);
					Motor.B.rotate(command[2], true);
					while (Motor.A.isMoving() || Motor.B.isMoving()) {
					}
					sendData(13, 1, 0);
					break;
				default:
					break;
				}
			case 5:
				switch (command[2]) {
				case 1:
					linearAcceleration = command[3];
					break;
				case 2:
					rotationAcceleration = command[3];
					break;
				case 3:
					rotationSpeed = command[3];
					break;
				}
			}
			Delay.msDelay(1);
			break;
		case 10:
			SensorPort sp = null;

			switch (command[2]) {
			case 1:
				sp = SensorPort.S1;
				break;
			case 2:
				sp = SensorPort.S2;
				break;
			case 3:
				sp = SensorPort.S3;
				break;
			case 4:
				sp = SensorPort.S4;
				break;
			}
			switch (command[1]) {
			case 1:
				distnx = new DistNxProcessingBrick(this, sp);
				break;
			case 2:
				lsa = new LSAProcessingBrick(this, sp);
				break;
			case 3:
				switch (command[3]) {
				case 1:
					leftEOPD = new EOPDProcessingBrick(this, sp, 3, true);
					break;
				case 2:
					rightEOPD = new EOPDProcessingBrick(this, sp, 4, true);
					break;
				default:
					break;
				}
				break;
			case 4:
				abs_imu = new Abs_ImuProcessingBrick(this, sp);
				break;
			case 5:
				thermal = new ThermalSensorProcessingBrick(this, sp);
				break;
			case 6:
				colorS = new ColorSensor(sp);
				break;
			default:
				break;
			}
			break;
		case 11:
			for (int j = 0; j < 5; j++) {
				colorS.setFloodlight(Color.BLUE);
				colorS.setFloodlight(Color.GREEN);
				colorS.setFloodlight(Color.RED);
				Delay.msDelay(500);
				colorS.setFloodlight(Color.NONE);
				Delay.msDelay(500);
			}
			break;

		case 99:

			com.closeConnection();
			System.exit(0);
			break;
		default:
			break;
		}
	}

	public void sendData(int sensorID, int sourceOfData, int value) {

		String sendString = "*" + sensorID + ";" + sourceOfData + ";" + value
				+ "#";
		com.sendString(sendString);
	}

	public void sendData(int sensorID, int sourceOfData, float value) {

		String sendString = "*" + sensorID + ";" + sourceOfData + ";" + value
				+ "#";
		com.sendString(sendString);
	}

}
