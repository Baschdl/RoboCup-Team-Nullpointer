package de.null_pointer.testmodules.testcommunication;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.testmodules.virtualhardware.VirtualMotor;

public class TestBrickControlPi extends BrickControlPi {

	private VirtualMotor motorA = new VirtualMotor();
	private VirtualMotor motorB = new VirtualMotor();
	private VirtualMotor motorC = new VirtualMotor();

	private static Logger logger = Logger.getLogger(TestBrickControlPi.class);

	@Override
	public void forward(int speed, char motorport) {
		VirtualMotor tempMotor = resolveMotorport(motorport);
		tempMotor.setSpeed(speed);
		tempMotor.forward();
	}

	@Override
	public void backward(int speed, char motorport) {
		VirtualMotor tempMotor = resolveMotorport(motorport);
		tempMotor.setSpeed(speed);
		tempMotor.backward();
	}

	@Override
	public void stop(char motorport) {
		VirtualMotor tempMotor = resolveMotorport(motorport);
		tempMotor.stop();
	}

	@Override
	public void flt(char motorport) {
		VirtualMotor tempMotor = resolveMotorport(motorport);
		tempMotor.flt();
	}

	@Override
	public void rotate(int angle, char motorport) {
		VirtualMotor tempMotor = resolveMotorport(motorport);
		tempMotor.rotate(angle);
	}

	protected VirtualMotor resolveMotorport(char motorport) {
		if (motorport == 'a' || motorport == 'A') {
			return motorA;
		} else if (motorport == 'b' || motorport == 'B') {
			return motorB;
		} else if (motorport == 'c' || motorport == 'C') {
			return motorC;
		} else {
			logger.error("Motorport konnte keinem gueltigen VirtualMotor zugeordnet werden");
			return null;
		}
	}
}
