package de.null_pointer.testmodules.testcommunication;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.communication_pi.CommunicationPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.pi_server.InitializeProgram;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.AccumulatorProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.sensorprocessing_pi.ThermalSensorProcessingPi;
import de.null_pointer.testmodules.virtualhardware.VirtualMotor;

public class TestBrickControlPi extends BrickControlPi {

	private VirtualMotor motorA = new VirtualMotor();
	private VirtualMotor motorB = new VirtualMotor();
	private VirtualMotor motorC = new VirtualMotor();

	private CommunicationPi com = null;

	public CommunicationPi getCom() {
		return com;
	}

	public TestBrickControlPi(CommunicationPi com, Navigation nav,
			Abs_ImuProcessingPi abs_Imu, DistNxProcessingPi distNx,
			EOPDProcessingPi eopdLeft, EOPDProcessingPi eopdRight,
			LSAProcessingPi lsa, AccumulatorProcessingPi accumulator,
			ThermalSensorProcessingPi thermal,
			InitializeProgram initializeProgram) {

		super(com, nav, abs_Imu, distNx, eopdLeft, eopdRight, lsa, accumulator,
				thermal, initializeProgram);
		super.setSensorReady();
		this.com = com;
	}

	// Getter werden zum Testen der Klasse benoetigt
	public VirtualMotor getMotorA() {
		return motorA;
	}

	public VirtualMotor getMotorB() {
		return motorB;
	}

	public VirtualMotor getMotorC() {
		return motorC;
	}

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
