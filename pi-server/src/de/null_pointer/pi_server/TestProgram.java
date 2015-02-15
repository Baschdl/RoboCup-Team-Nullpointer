package de.null_pointer.pi_server;

import java.util.Properties;

import org.apache.log4j.Logger;

import lejos.util.Delay;
import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;

public class TestProgram {

	private static Logger logger = Logger.getLogger(TestProgram.class);

	private InitializeProgram initProgram = null;
	private MotorControlPi motorcontrol = null;
	private BrickControlPi brickCon1 = null;
	private BrickControlPi brickCon2 = null;

	private int speedLinear = -1;
	private int speedTurn = -1;
	private int difference = -1;
	private int duration = -1;

	public TestProgram(InitializeProgram initProgram, Properties propPiServer) {

		this.initProgram = initProgram;
		brickCon1 = this.initProgram.getBrickCon1();
		brickCon2 = this.initProgram.getBrickCon2();
		motorcontrol = this.initProgram.getMotorControl();

		speedLinear = Integer.parseInt(propPiServer
				.getProperty("Pi_server.TestProgram.linear.speed"));
		speedTurn = Integer.parseInt(propPiServer
				.getProperty("Pi_server.TestProgram.turn.speed"));
		difference = Integer.parseInt(propPiServer
				.getProperty("Pi_server.TestProgram.difference"));
		duration = Integer.parseInt(propPiServer
				.getProperty("Pi_server.TestProgram.duration"));
	}

	public void forward(int speed) {
		logger.debug("Testprogramm: Vorwaerts");
		if (speed != 0) {
			motorcontrol.forward(speed);
		} else {
			motorcontrol.forward(speedLinear);
		}
		logger.debug("Testprogramm: Delay");
		Delay.msDelay(duration);
		logger.debug("Testprogramm: Flt");
		motorcontrol.flt();

	}

	public void backward(int speed) {
		if (speed != 0) {
			motorcontrol.forward(speed);
		} else {
			motorcontrol.forward(speedLinear);
		}
		Delay.msDelay(duration);
		motorcontrol.flt();

	}

	public void rightturn(int angle) {
		if (angle != 0) {
			motorcontrol.rotateright(angle);
		} else {
			motorcontrol.rotateright(90);
		}
		Delay.msDelay(duration);
		motorcontrol.flt();

	}

	public void leftturn(int angle) {
		if (angle != 0) {
			motorcontrol.rotateright(angle);
		} else {
			motorcontrol.rotateright(90);
		}
		Delay.msDelay(duration);
		motorcontrol.flt();

	}

	public void flash() {
		// hardcoded
		brickCon2.blinkColorSensorLED();
	}

}
