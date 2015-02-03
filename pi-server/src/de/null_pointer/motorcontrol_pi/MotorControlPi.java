package de.null_pointer.motorcontrol_pi;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;

public class MotorControlPi {
	private static Logger logger = Logger.getLogger(MotorControlPi.class);

	private BrickControlPi brickCon1;
	private BrickControlPi brickCon2;

	// actualSpeed: positive: speed forward, negative: speed backward
	private int actualSpeed = -1;
	// mode: forward: 0, backward: 1, rightturn: 2, leftturn: 3
	private int mode = -1;
	private int speedCurve = -1;
	private int speedDifference = -1;
	private int notMoving = -1;

	// Getters werden zum Testen benoetigt
	public int getActualSpeed() {
		return actualSpeed;
	}

	public int getMode() {
		return mode;
	}

	public int getSpeedCurve() {
		return speedCurve;
	}

	public int getSpeedDifference() {
		return speedDifference;
	}

	public int getNotMoving() {
		return notMoving;
	}

	public MotorControlPi(BrickControlPi brickCon1, BrickControlPi brickCon2) {
		this.brickCon1 = brickCon1;
		this.brickCon2 = brickCon2;
	}

	/**
	 * Methode zum vorwaerts Fahren des Roboters.
	 * 
	 * @param speed
	 *            Geschwindigkeit des Fahrens
	 */
	// synchronized noetig?
	public void forward(int speed) {
		if (actualSpeed != (speed) && mode != 0) {
			notMoving = 1;
			speedDifference = 1;
			speedCurve = 1;

			logger.info("PC set all motors forward speed " + speed);
			brickCon2.forward(speed, 'A');
			brickCon2.backward(speed, 'B');
			brickCon1.stop('A');
			brickCon1.stop('B');
			actualSpeed = speed;
			mode = 0;
		}
	}

	/**
	 * Methode zum rueckwaerts Fahren des Roboters.
	 * 
	 * @param speed
	 *            Geschwindigkeit des Fahrens
	 */
	// synchronized noetig?
	public void backward(int speed) {
		if (actualSpeed != -(speed) && mode != 1) {
			notMoving = 1;
			speedDifference = 1;
			speedCurve = 1;

			logger.info("PC set all motors backward speed " + speed);
			brickCon2.backward(speed, 'A');
			brickCon2.forward(speed, 'B');
			brickCon1.stop('A');
			brickCon1.stop('B');
			actualSpeed = -speed;
			mode = 1;
		}
	}

	/**
	 * Methode zum Fahren einer Rechtskurve.
	 * 
	 * @param speed
	 *            Geschwindigkeit der Rechtskurve
	 * @param difference
	 *            Unterschied der Geschwindigkeit der linken und rechten
	 *            Motoren. Beeinflusst den Wendekreis.
	 */
	// TODO: Loeschen
	@SuppressWarnings("deprecation")
	public void rightturn(int speed, int difference) {
		if (this.speedCurve != speed && this.speedDifference != difference
				&& mode != 2) {
			notMoving = 1;
			actualSpeed = 1;

			logger.info("PC set rightcurve speed " + speed + " difference "
					+ difference);
			// TODO: Unserem aktuellen Roboter anpassen
			// brickCon1.backward(speed, 'A');
			// brickCon1.backward(speed - difference, 'B');
			// brickCon2.forward(speed, 'A');
			// brickCon2.forward(speed - difference, 'B');
			// brickCon2.forward(speed, 'A');
			// brickCon2.forward(speed, 'B');
			// brickCon2.forward(speed, 'C');
			this.speedDifference = difference;
			this.mode = 2;
			this.speedCurve = speed;
		}
	}

	/**
	 * Methode zum Fahren einer Linkskurve.
	 * 
	 * @param speed
	 *            Geschwindigkeit der Linkskurve
	 * @param difference
	 *            Unterschied der Geschwindigkeit der linken und rechten
	 *            Motoren. Beeinflusst den Wendekreis.
	 */
	// TODO: Loeschen
	@SuppressWarnings("deprecation")
	public void leftturn(int speed, int difference) {
		if (this.speedCurve != speed && this.speedDifference != difference
				&& mode != 3) {
			notMoving = 1;
			actualSpeed = 1;

			logger.info("PC set leftcurve speed " + speed + " difference "
					+ difference);
			// TODO: Unserem aktuellen Roboter anpassen
			// brickCon1.backward(speed - difference, 'A');
			// brickCon1.backward(speed, 'B');
			// brickCon2.forward(speed - difference, 'A');
			// brickCon2.forward(speed, 'B');
			this.speedDifference = -difference;
			this.mode = 3;
			this.speedCurve = -speed;
		}
	}

	/**
	 * Methode zum Rotieren des Roboters auf der Stelle nach rechts.
	 * 
	 * @param angle
	 *            Winkel der Rotation
	 */
	public void rotateright(int angle) {
		notMoving = 1;
		speedDifference = 1;
		speedCurve = 1;
		actualSpeed = 1;
		
		int wheelAngle = (int) Math.round(((Math.PI*16.8/(360/angle))*360/Math.PI*4.8)/2);

		logger.info("PC set motor rotate left angle " + angle);
		brickCon1.rotate(wheelAngle, 'A');
		brickCon1.rotate(wheelAngle, 'B');
		brickCon2.rotate(wheelAngle, 'A');
		brickCon2.rotate(wheelAngle, 'B');
	}

	/**
	 * Methode zum Rotieren des Roboters auf der Stelle nach links.
	 * 
	 * @param angle
	 *            angle Winkel der Rotation
	 */
	public void rotateleft(int angle) {
		notMoving = 1;
		speedDifference = 1;
		speedCurve = 1;
		actualSpeed = 1;
		
		int wheelAngle = (int) Math.round(((Math.PI*16.8/(360f/angle))*360/Math.PI*4.8)/2);

		logger.info("PC set motor rotate left angle " + angle);
		brickCon1.rotate(-wheelAngle, 'A');
		brickCon1.rotate(-wheelAngle, 'B');
		brickCon2.rotate(-wheelAngle, 'A');
		brickCon2.rotate(-wheelAngle, 'B');
	}

	/**
	 * Methode zum Stoppen der Motoren, sodass sich die Raeder nicht mehr weiter
	 * drehen
	 */
	public void stop() {
		if (notMoving != -10) {
			speedDifference = 1;
			speedCurve = 1;
			actualSpeed = 1;

			logger.info("PC set all motor stop");
			brickCon1.stop('A');
			brickCon1.stop('B');
			brickCon2.stop('A');
			brickCon2.stop('B');
			notMoving = -10;

		}
	}

	/**
	 * Methode zum weichen Abbremsen der Motoren, die Raeder "rollen" aus
	 */
	public void flt() {
		if (notMoving != -10) {
			logger.info("PC set all motor flt");
			speedDifference = 1;
			speedCurve = 1;
			actualSpeed = 1;
			brickCon1.flt('A');
			brickCon1.flt('B');
			brickCon2.flt('A');
			brickCon2.flt('B');
			notMoving = -10;

		}
	}

}
