package de.null_pointer.motorcontrol_pi;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;

/**
 * Layer of abstraction for the motors to control our individual robot
 * 
 * @author Sebastian Bischoff (sebastianbischoff@null-pointer.de) ("Baschdl" on
 *         the leJOS-Forum), Jan Krebes (jankrebes@null-pointer.de), Samuel
 *         Scherer (samuelscherer@null-pointer.de)
 * 
 */
public class MotorControlPi {
	private static Logger logger = Logger.getLogger(MotorControlPi.class);

	private BrickControlPi brickCon1 = null;
	private BrickControlPi brickCon2 = null;

	// actualSpeed:
	// positive: speed forward, negative: speed backward
	private int actualSpeed = -1;

	// mode: forward: 0, backward: 1, rightturn: 2, leftturn: 3, stop: 4,
	// float: 5
	private int mode = -1;

	private int rotationHeading = 0;

	// Getters needed for testing purposes
	public int getActualSpeed() {
		return actualSpeed;
	}

	public int getMode() {
		return mode;
	}

	public int getRotationHeading() {
		return rotationHeading;
	}

	public MotorControlPi(BrickControlPi brickCon1, BrickControlPi brickCon2) {
		this.brickCon1 = brickCon1;
		this.brickCon2 = brickCon2;
	}

	/**
	 * Method to drive forward
	 * 
	 * @param speed
	 *            moving speed
	 */
	// synchronized noetig?
	public void forward(int speed) {
		if (actualSpeed != (speed) && mode != 0) {
			logger.info("PC set all motors forward speed " + speed);
			brickCon2.forward(speed, 'D');
			brickCon1.stop('D');
			actualSpeed = speed;
			mode = 0;
		}
	}

	/**
	 * Method to drive backward
	 * 
	 * @param speed
	 *            moving speed
	 */
	// synchronized noetig?
	public void backward(int speed) {
		if (actualSpeed != -(speed) && mode != 1) {
			logger.info("PC set all motors backward speed " + speed);
			brickCon2.backward(speed, 'D');
			brickCon1.stop('D');
			actualSpeed = -speed;
			mode = 1;
		}
	}

	/**
	 * method to change forward-speed on only one motor
	 * 
	 * @param brick
	 *            Brick of the controlled motor
	 * @param motorport
	 *            Port of the brick of the controlled motor
	 * @param speed
	 *            New speed of the motor
	 */
	public void changeSpeedSingleMotorForward(int brick, char motorport,
			int speed) {
		if (brick == 1) {
			brickCon1.forward(speed, motorport);
		} else if (brick == 2) {
			brickCon2.forward(speed, motorport);
		}
	}

	/**
	 * method to change backward-speed on only one motor
	 * 
	 * @param brick
	 *            Brick of the controlled motor
	 * @param motorport
	 *            Port of the brick of the controlled motor
	 * @param speed
	 *            New speed of the motor
	 */
	public void changeSpeedSingleMotorBackward(int brick, char motorport,
			int speed) {
		if (brick == 1) {
			brickCon1.backward(speed, motorport);
		} else if (brick == 2) {
			brickCon2.backward(speed, motorport);
		}
	}

	/**
	 * Method to rotate around the vertical-axis clockwise
	 * 
	 * @param angle
	 *            rotation angle
	 */
	public void rotateright(int angle) {
		actualSpeed = 1;
		mode = 2;

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
	 * Method to rotate around the vertical-axis anti-clockwise
	 * 
	 * @param angle
	 *            rotation angle
	 */
	public void rotateleft(int angle) {
		actualSpeed = 1;
		mode = 3;

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
	 * Method to stop the motors, active breaking with the stepping motor
	 */
	public void stop() {
		if (mode != 4 && mode != 5) {
			actualSpeed = 1;
			mode = 4;

			logger.info("PC set all motor stop");
			brickCon1.stop('D');
			brickCon2.stop('D');

		}
	}

	/**
	 * Method to stop the motors, no active breaking, wheels can roll out
	 */
	public void flt() {
		if (mode != 4 && mode != 5) {
			logger.info("PC set all motor flt");
			actualSpeed = 1;
			mode = 5;
			brickCon1.flt('D');
			brickCon2.flt('D');

		}
	}

	/**
	 * Method to decide in which direction, depending on the current heading,
	 * the robot has to move
	 * 
	 * @param heading
	 *            Actual heading determined by the AbsIMU or Motorcontrol
	 * @param directionToMove
	 *            Direction in which the robot has to move
	 */
	public void decideTurn(int heading, int directionToMove) {

		if (heading == 0) {
			if (directionToMove == 0) {
				return;
			} else if (directionToMove == 1) {
				rotateright(90);
			} else if (directionToMove == 2) {
				rotateright(180);
			} else if (directionToMove == 3) {
				rotateleft(90);
			}
		} else if (heading == 1) {
			if (directionToMove == 0) {
				rotateleft(90);
			} else if (directionToMove == 1) {
				return;
			} else if (directionToMove == 2) {
				rotateright(90);
			} else if (directionToMove == 3) {
				rotateright(180);
			}
		} else if (heading == 2) {
			if (directionToMove == 0) {
				rotateright(180);
			} else if (directionToMove == 1) {
				rotateleft(90);
			} else if (directionToMove == 2) {
				return;
			} else if (directionToMove == 3) {
				rotateright(90);
			}
		} else if (heading == 3) {
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

	/**
	 * Changes the heading when the robot rotates
	 * 
	 * @param right
	 *            With right=true the heading is changed clockwise
	 */
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
