package de.null_pointer.pi_server;

import lejos.util.Delay;
import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;

public class TestProgram {

	private InitializeProgram initProgram = null;
	private MotorControlPi motorcontrol = null;
	private BrickControlPi brickCon1 = null;
	private BrickControlPi brickCon2 = null;

	private int speedLinear = -1;
	private int speedTurn = -1;
	private int difference = -1;
	private int duration = -1;

	public TestProgram(InitializeProgram initProgram, int speedLinear,
			int speedTurn, int difference, int duration) {
		this.initProgram = initProgram;
		brickCon1 = this.initProgram.getBrickCon1();
		brickCon2 = this.initProgram.getBrickCon2();
		motorcontrol = this.initProgram.getMotorControl();

		this.speedLinear = speedLinear;
		this.speedTurn = speedTurn;
		this.difference = difference;
		this.duration = duration;
	}

	public void forward() {
		motorcontrol.forward(speedLinear);
		Delay.msDelay(duration);
		motorcontrol.flt();

	}

	public void backward() {
		motorcontrol.backward(speedLinear);
		Delay.msDelay(duration);
		motorcontrol.flt();

	}

	public void rightturn() {
		motorcontrol.rightturn(speedTurn, difference);
		Delay.msDelay(duration);
		motorcontrol.flt();

	}

	public void leftturn() {
		motorcontrol.leftturn(speedTurn, difference);
		Delay.msDelay(duration);
		motorcontrol.flt();

	}

}
