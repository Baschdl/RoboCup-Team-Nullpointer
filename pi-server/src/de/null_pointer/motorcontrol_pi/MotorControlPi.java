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

	private int rotationHeading = 0;

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
			brickCon2.forward(speed, 'D');
			brickCon1.stop('D');
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
			brickCon2.backward(speed, 'D');
			brickCon1.stop('D');
			actualSpeed = -speed;
			mode = 1;
		}
	}
	
	/**
	 * method to change forward-speed on only one motor
	 * @param brick 
	 * 			Brick of the controlled motor
	 * @param motorport
	 * 			Port of the brick of the controlled motor
	 * @param speed
	 * 			New speed of the motor
	 */
	public void changeSpeedSingleMotorForward(int brick, char motorport, int speed){
		if(brick == 1){
			brickCon1.forward(speed, motorport);
		}else if(brick == 2){
			brickCon2.forward(speed, motorport);
		}
	}
	/**
	  * method to change backward-speed on only one motor
	 * @param brick 
	 * 			Brick of the controlled motor
	 * @param motorport
	 * 			Port of the brick of the controlled motor
	 * @param speed
	 * 			New speed of the motor
	 */
	public void changeSpeedSingleMotorBackward(int brick, char motorport, int speed){
		if(brick == 1){
			brickCon1.backward(speed, motorport);
		}else if(brick == 2){
			brickCon2.backward(speed, motorport);
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

		int wheelAngle = (int) Math.round(((Math.PI * 16.8) / (360f / angle))
				* (360 / (Math.PI * 4.8)));

		logger.info("PC set motor rotate left angle " + angle);
		brickCon1.rotate(wheelAngle, 'D');
		brickCon2.rotate(wheelAngle, 'D');

		for (int i = 0; i < (angle / 90); i++) {
			turnRotationHeading(true);
		}
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

		int wheelAngle = (int) Math.round(((Math.PI * 16.8) / (360f / angle))
				* (360 / (Math.PI * 4.8)));

		logger.info("PC set motor rotate left angle " + angle);
		brickCon1.rotate(-wheelAngle, 'D');
		brickCon2.rotate(-wheelAngle, 'D');

		for (int i = 0; i < (angle / 90); i++) {
			turnRotationHeading(false);
		}
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
			brickCon1.stop('D');
			brickCon2.stop('D');
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
			brickCon1.flt('D');
			brickCon2.flt('D');
			notMoving = -10;

		}
	}

	public void decideTurn(int absImuHeading, int directionToMove) {

		if (absImuHeading == 0) {
			if (directionToMove == 0) {
				return;
			} else if (directionToMove == 1) {
				rotateright(90);
			} else if (directionToMove == 2) {
				rotateright(180);
			} else if (directionToMove == 3) {
				rotateleft(90);
			}
		} else if (absImuHeading == 1) {
			if (directionToMove == 0) {
				rotateleft(90);
			} else if (directionToMove == 1) {
				return;
			} else if (directionToMove == 2) {
				rotateright(90);
			} else if (directionToMove == 3) {
				rotateright(180);
			}
		} else if (absImuHeading == 2) {
			if (directionToMove == 0) {
				rotateright(180);
			} else if (directionToMove == 1) {
				rotateleft(90);
			} else if (directionToMove == 2) {
				return;
			} else if (directionToMove == 3) {
				rotateright(90);
			}
		} else if (absImuHeading == 3) {
			if (directionToMove == 0) {
				rotateright(90);
			} else if (directionToMove == 1) {
				rotateright(180);
			} else if (directionToMove == 2) {
				rotateleft(90);
			} else if (directionToMove == 3) {
				return;
			}
		}

	}

	public int getRotationHeading() {
		return rotationHeading;
	}

	private void turnRotationHeading(boolean right) {
		if (right) {
			if (rotationHeading == 3) {
				rotationHeading = 0;
			} else {
				rotationHeading++;
			}
		} else {
			if (rotationHeading == 0) {
				rotationHeading = 3;
			} else {
				rotationHeading--;
			}
		}
	}

}
