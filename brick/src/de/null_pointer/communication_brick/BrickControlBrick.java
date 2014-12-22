package de.null_pointer.communication_brick;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.addon.EOPD;
import lejos.nxt.addon.OpticalDistanceSensor;
import de.null_pointer.sensor.AbsoluteIMU_ACG;
import de.null_pointer.sensor.LightSensorArray;
import de.null_pointer.sensor.NXTVoltMeter;

public class BrickControlBrick extends Thread {

	private OpticalDistanceSensor dist_nx;
	private LightSensorArray lsa;
	private AbsoluteIMU_ACG abs_imu;
	private NXTVoltMeter nxt_vm;
	private EOPD eopdLeft;
	private EOPD eopdRight;
	private TouchSensor tsLeft;
	private TouchSensor tsRight;

	private SensorPort sp_dist_nx;
	private SensorPort sp_lsa;
	private SensorPort sp_abs_imu;
	private SensorPort sp_nxt_vm;
	private SensorPort sp_eopdLeft;
	private SensorPort sp_eopdRight;
	private SensorPort sp_tsLeft;
	private SensorPort sp_tsRight;

	CommunicationBrick com = null;

	// ThreadSensor ths;

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
				// TODO: Abfrage der Sensoren
				// letztes Jahr wurde dies in einer eigenen Klasse geregelt
				//
				// ths = new ThreadSensor(this, lsa, abs_imu, tsLeft, tsRight,
				// eopdLeft, eopdRight, dist_nx, nxt_vm);
				// ths.start();
				// ths.setDaemon(true);
				i++;
			} else {
				this.receiveCommand();
			}
		}
	}

	public synchronized void receiveCommand() {
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
		// array target,
		// sendData("Brick processCommand" + Arrays.toString(command));
		switch (command[0]) {
		case 1:
			switch (command[1]) {
			case 1:
				dist_nx.powerOn();
				break;
			case 2:
				dist_nx.powerOff();
				break;
			case 3:
				sendData(1, 3, dist_nx.getRange());
				break;
			case 4:
				float[] buf_Ranges = dist_nx.getRanges();
				for (int i = 0, j = 4; i < buf_Ranges.length; i++, j++) {
					sendData(1, j, buf_Ranges[i]);
				}
				break;
			case 5:
				sendData(1, 5, dist_nx.getSensorModule());
				break;
			default:
				break;
			}
			break;
		case 2:
			switch (command[1]) {
			case 1:
				sendData(2, 10, lsa.wakeUp());
				break;
			case 2:
				sendData(2, 11, lsa.sleep());
				break;
			case 3:
				sendData(2, 12, lsa.calibrateBlack());
				break;
			case 4:
				sendData(2, 13, lsa.calibrateWhite());
				break;
			case 5:
				sendData(2, 9, lsa.getLightValue(command[2]));
				break;
			}
			break;
		case 3:
			switch (command[1]) {
			case 1:
				eopdLeft.setModeShort();
				break;
			case 2:
				eopdLeft.setModeLong();
				break;
			}
			break;
		case 4:
			switch (command[1]) {
			case 1:
				eopdRight.setModeShort();
				break;
			case 2:
				eopdRight.setModeLong();
				break;
			}
			break;
		case 5:
			switch (command[1]) {
			case 1:
				int[] buf_Tilt = abs_imu.getTiltData();
				for (int i = 0; i < buf_Tilt.length; i++) {
					sendData(5, i + 1, buf_Tilt[i]);
				}
				break;
			case 2:
				int[] buf_Accel = abs_imu.getAcceleration();
				for (int i = 0, j = 4; i < buf_Accel.length; i++, j++) {
					sendData(5, j, buf_Accel[i]);
				}
				break;
			case 3:
				sendData(5, 7, abs_imu.getCompassHeading());
				break;
			case 4:
				int[] buf_MagField = abs_imu.getMagneticField();
				for (int i = 0, j = 8; i < buf_MagField.length; i++, j++) {
					sendData(5, j, buf_MagField[i]);
				}
				break;
			case 5:
				int[] buf_Gyro = abs_imu.getGyro();
				for (int i = 0, j = 11; i < buf_Gyro.length; i++, j++) {
					sendData(5, j, buf_Gyro[i]);
				}
				break;
			case 6:
				sendData(5, 14, abs_imu.getFilter());
				break;
			case 7:
				sendData(5, 15, abs_imu.setFilter(command[2]));
				break;
			}
			break;
		case 8:
			switch (command[1]) {
			case 1:
				sendData(8, 1, nxt_vm.getAbsoluteVoltage());
				break;
			case 2:
				sendData(8, 1, nxt_vm.getRelativeVoltage());
				break;
			case 3:
				sendData(8, 1, nxt_vm.getReferenceVoltage());
				break;
			case 4:
				sendData(8, 1, nxt_vm.setReferenceVoltage(command[2]));
				break;
			case 5:
				sendData(8, 1, nxt_vm.writeCurrentAbsoluteVoltage());
				break;
			}
			break;
		case 9:
			switch (command[1]) {
			case 1:
				if (command[2] != 0 && command[3] != 5) {
					Motor.A.setSpeed(command[2]);
				}
				switch (command[3]) {
				case 1:
					Motor.A.forward();
					break;
				case 2:
					Motor.A.backward();
					break;
				case 3:
					Motor.A.stop();
					Motor.B.stop();
					break;
				case 4:
					Motor.A.flt();
					break;
				case 5:
					Motor.A.rotate(command[2], true);
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
					Motor.B.forward();
					break;
				case 2:
					Motor.B.backward();
					break;
				case 3:
					Motor.B.stop();
					Motor.A.stop();

					break;
				case 4:
					Motor.B.flt();
					break;
				case 5:
					Motor.B.rotate(command[2], true);
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
					Motor.C.forward();
					break;
				case 2:
					Motor.C.backward();
					break;
				case 3:
					Motor.C.stop();
					break;
				case 4:
					Motor.C.flt();
					break;
				case 5:
					Motor.C.rotate(command[2], true);
					break;
				case 6:
					Motor.C.resetTachoCount();
					break;
				default:
					break;
				}
				break;

			}
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
				// sendData("Init - ODS start ");
				sp_dist_nx = sp;
				dist_nx = new OpticalDistanceSensor(sp_dist_nx);
				// sendData("Init - ODS end ");
				break;
			case 2:
				// sendData("Init LightSensorArray ");
				sp_lsa = sp;
				lsa = new LightSensorArray(sp_lsa);
				// initLsaThread();
				break;
			case 3:
				switch (command[3]) {
				case 1:
					// sendData("Init EOPD left");
					sp_eopdLeft = sp;
					eopdLeft = new EOPD(sp_eopdLeft);

					// SensorPort.S3.addSensorPortListener(sensor3);

					break;

				case 2:
					// sendData("Init EOPD right ");
					sp_eopdRight = sp;
					eopdRight = new EOPD(sp_eopdRight);
					break;
				default:
					break;
				}
				// if (sp == SensorPort.S1) {
				// SensorPort.S1.addSensorPortListener(sensor1);
				// } else
				// if (sp == SensorPort.S2) {
				// SensorPort.S2.addSensorPortListener(sensor2);
				// }
				// else if (sp == SensorPort.S3) {
				// SensorPort.S3.addSensorPortListener(sensor3);
				// } else if (sp == SensorPort.S4) {
				// SensorPort.S4.addSensorPortListener(sensor4);
				// }
				break;
			case 4:
				// sendData("Init Absolute IMU ");
				sp_abs_imu = sp;
				abs_imu = new AbsoluteIMU_ACG(sp_abs_imu);
				break;
			case 5:

				switch (command[3]) {
				case 1:
					// sendData("Init Touchsensor 1");
					sp_tsLeft = sp;
					tsLeft = new TouchSensor(sp_tsLeft);
					break;
				case 2:
					sp_tsRight = sp;
					// sendData("Init Touchsensor 2");
					tsRight = new TouchSensor(sp_tsRight);
					break;
				default:
					// sendData("Init - default should not be called 2");
					break;
				}
				// if (sp == SensorPort.S1) {
				// SensorPort.S1.addSensorPortListener(sensor1);
				// } else if (sp == SensorPort.S2) {
				// SensorPort.S2.addSensorPortListener(sensor2);
				// } else if (sp == SensorPort.S3) {
				// SensorPort.S3.addSensorPortListener(sensor3);
				// } else if (sp == SensorPort.S4) {
				// SensorPort.S4.addSensorPortListener(sensor4);
				// }
				break;
			case 6:
				// sendData("Init Voltmeter ");
				sp_nxt_vm = sp;
				nxt_vm = new NXTVoltMeter(sp_nxt_vm);
				break;
			default:
				// sendData("Init - default should not be called 3");
				break;
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

	// public void sendData(String message) {
	// try {
	// datatoPi.writeUTF("*" + 90 + ";" + 90 + "; NXT: " + message + "#");
	// datatoPi.flush();
	// } catch (IOException e) {
	// System.err.println("IO Exception writing data");
	// }
	// }

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

	// Uebergabe von SensorPortListener-Wert
	public void processData(SensorPort port) {
		try {
			if (port == sp_dist_nx) {
				sendData(1, 1, dist_nx.getDistance());
			} else if (port == sp_lsa) {
				int buf[] = lsa.getLightValues();
				for (int i = 0; i < 8; i++) {
					sendData(2, i + 1, buf[i]);
				}
			} else if (port == sp_abs_imu) {
				int buf[] = abs_imu.getTiltData();
				for (int i = 0; i < 3; i++) {
					sendData(5, i + 1, buf[i]);
				}
			} else if (port == sp_nxt_vm) {
				sendData(8, 1, nxt_vm.getAbsoluteVoltage());
			} else if (port == sp_eopdLeft) {
				sendData(3, 1, eopdLeft.readRawValue());
			} else if (port == sp_eopdRight) {
				sendData(4, 1, eopdRight.readRawValue());
			} else if (port == sp_tsLeft) {
				if (tsLeft.isPressed()) {
					sendData(6, 1, 1);
				} else {
					sendData(6, 1, 0);
				}
			} else if (port == sp_tsRight) {
				if (tsRight.isPressed()) {
					sendData(7, 1, 1);
				} else {
					sendData(7, 1, 0);
				}
			} else {
				System.out.println("Nicht bekannter Port");
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
