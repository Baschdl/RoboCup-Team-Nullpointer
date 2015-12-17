package de.null_pointer.communication_pi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.testmodules.testcommunication.TestBrickControlPi;
import lejos.util.Delay;

public class EmulateSensordata extends Thread {
	private CommunicationPi comPi1 = null;
	private CommunicationPi comPi2 = null;

	private LinkedList<Long> timeFromFile = new LinkedList<Long>();
	private LinkedList<String> dataFromFile = new LinkedList<String>();

	private BrickControlPi virtBrick = new BrickControlPi();

	private long oldTime = -1;
	private TestBrickControlPi brickControlPi1 = null;
	private TestBrickControlPi brickControlPi2 = null;

	public EmulateSensordata(BrickControlPi brickControlPi1,
			BrickControlPi brickControlPi2) {
		this.brickControlPi1 = (TestBrickControlPi) brickControlPi1;
		this.brickControlPi2 = (TestBrickControlPi) brickControlPi2;

		this.comPi1 = this.brickControlPi1.getCom();
		this.comPi2 = this.brickControlPi2.getCom();
		readFile();
		oldTime = timeFromFile.getFirst();
	}

	@Override
	public void run() {
		long pufTime = -1;
		String pufData = null;
		int sensorNumber = -1;
		while (timeFromFile.size() != 0) {
			Delay.msDelay((pufTime = timeFromFile.removeFirst()) - oldTime);
			sensorNumber = Math.round(virtBrick
					.checkString((pufData = dataFromFile.removeFirst()))[0]);
			if (sensorNumber == BrickControlPi.Sensor.DistNx.getNumber()) {

			} else if (sensorNumber == BrickControlPi.Sensor.LightSensorArray
					.getNumber()) {

			} else if (sensorNumber == BrickControlPi.Sensor.EOPDLinks
					.getNumber()) {

			} else if (sensorNumber == BrickControlPi.Sensor.EOPDRechts
					.getNumber()) {

			} else if (sensorNumber == BrickControlPi.Sensor.AbsoluteIMU
					.getNumber()) {

			} else if (sensorNumber == BrickControlPi.Sensor.IRThermalSensor
					.getNumber()) {

			}
			// TODO: Akku, genuegend Werte, Karte laden und Programm starten,
			// Semaphore fehlen
			oldTime = pufTime;
		}

	}

	private void readFile() {
		BufferedReader br = null;
		String readData = null;
		int indexSeparator = -1;
		try {
			br = new BufferedReader(new FileReader("sensordata3.log"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while ((readData = br.readLine()) != null) {
				timeFromFile.add(Long.parseLong(readData.substring(0,
						(indexSeparator = readData.indexOf(":")))));
				System.out.println(timeFromFile.getLast());
				dataFromFile.add(readData.substring(indexSeparator + 1,
						readData.length()));
				System.out.println(dataFromFile.getLast());
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
