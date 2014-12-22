package de.null_pointer.pi_server;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;

public class TestProgram {

	private InitializeProgram initProgram = null;
	private MotorControlPi motorcontrol = null;
	private BrickControlPi brickCon1 = null;
	private BrickControlPi brickCon2 = null;

	public TestProgram(InitializeProgram initProgram) {
		this.initProgram = initProgram;
		brickCon1 = this.initProgram.getBrickCon1();
		brickCon2 = this.initProgram.getBrickCon2();
		motorcontrol = this.initProgram.getMotorControl();
	}

	public void forward() {
		motorcontrol.forward(200);
		try {
			wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		motorcontrol.flt();

	}

	public void backward() {
		motorcontrol.backward(200);
		try {
			wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		motorcontrol.flt();

	}

	public void rightturn() {
		motorcontrol.rightturn(200, 50);
		try {
			wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		motorcontrol.flt();

	}

	public void leftturn() {
		motorcontrol.leftturn(200, 50);
		try {
			wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		motorcontrol.flt();

	}

}
