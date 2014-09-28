package de.null_pointer.testmodules.virtualhardware;

public class VirtualMotor {

	public VirtualMotor() {
	}

	protected static final int FORWARD = 1;
	protected static final int BACKWARD = 2;
	protected static final int STOP = 3;
	protected static final int FLOATING = 4;

	protected int mode = STOP;
	private boolean moving = false;
	private boolean stalled = false;
	private int speed = 360;
	private int angle = 0;

	/**
	 * Returns the mode of the motor.
	 * 
	 * forward: 1, backward: 2, stop: 3, floating: 4
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Returns if the motor is moving.
	 */
	public boolean isMoving() {
		return moving;
	}

	/**
	 * Return if the Motor is stalled.
	 */
	public boolean isStalled() {
		return stalled;
	}

	/**
	 * Sets motor speed , in degrees per second; Up to 900 is possible with 8
	 * volts.
	 */
	public void setSpeed(int pSpeed) {
		speed = pSpeed;
	}

	/**
	 * Returns the speed of the motor
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Causes motor to rotate forward.
	 */
	public void forward() {
		mode = FORWARD;
		moving = true;
	}

	/**
	 * Causes motor to rotate backward.
	 */
	public void backward() {
		mode = BACKWARD;
		moving = true;
	}

	/**
	 * causes motor to rotate through angle.
	 */
	public void rotate(int pAngle) {
		if ((pAngle + angle) < 360) {
			angle = angle + pAngle;
		} else {
			angle = pAngle + angle - 360;
		}
	}

	/**
	 * causes motor to rotate through angle; if immediateReturn is true, method
	 * returns immediately and the motor stops by itself If any motor method is
	 * called before the limit is reached, the rotation is canceled.
	 */
	public void rotate(int pAngle, boolean immediateReturn) {
		if (immediateReturn == false) {
			if ((pAngle + angle) < 360) {
				angle = angle + pAngle;
			} else {
				angle = pAngle + angle - 360;
			}
		} else {
			stop();
		}
	}

	/**
	 * causes motor to rotate to limitAngle; Then getTachoCount should be within
	 * +- 2 degrees of the limit angle when the method returns
	 */
	public void rotateTo(int limitAngle) {
		int puffer = limitAngle / 360;
		angle = limitAngle - puffer * 360;
	}

	/**
	 * causes motor to rotate to limitAngle; if immediateReturn is true, method
	 * returns immediately and the motor stops by itself and getTachoCount
	 * should be within +- 2 degrees if the limit angle If any motor method is
	 * called before the limit is reached, the rotation is canceled.
	 */
	public void rotateTo(int limitAngle, boolean immediateReturn) {
		if (immediateReturn == false) {
			int puffer = limitAngle / 360;
			angle = limitAngle - puffer * 360;
		} else {
			stop();
		}
	}

	/**
	 * Causes motor to stop.
	 */
	public void stop() {
		mode = STOP;
		moving = false;
	}

	/**
	 * Causes motor to float.
	 */
	public void flt() {
		mode = FLOATING;
		moving = false;
	}

	// public void setBrakePower(int pwr) {
	// }
	//
	// public void setPower(int power) {
	// }
	// // sets motor power.
	//
	// public void shutdown() {
	// }
	// // causes run() to exit
	//
	// public void smoothAcceleration(boolean yes) {
	// }
	// // enables smoother acceleration.
	//
	// public void reverseDirection() {
	// }
	// // Reverses direction of the motor.
	//
	// public void regulateSpeed(boolean yes) {
	// isRegulatedSpeed = yes;
	// }
	// public boolean is_regulateSpeed(){
	// return isRegulatedSpeed;
	// }
	// // turns speed regulation on/off;
	// // Cumulative speed error is within about 1 degree after initial
	// // acceleration.
	// public void lock(int power) {
	// isLocked = true;
	// lockPower = power;
	// }
	// public boolean is_locked(){
	// return isLocked;
	// }
	// public int getLockPower(){
	// return lockPower;
	// }
	// // Applies power to hold motor in current position.
	// public int getActualSpeed() {
	// return int;
	// }
	//
	// // returns actualSpeed degrees per second, calculated every 100 ms;
	// negative
	// // value means motor is rotating backward
	//
	// public float getBasePower() {
	// return float;
	// }
	// // for debugging
	//
	// public float getError() {
	// return float;
	// }
	// // for debugging
	//
	// public int getLimitAngle() {
	// return int;
	// }
	// // Return the angle that a Motor is rotating to.
	//
	// public int getMode() {
	// return int;
	// }
	// // Returns the mode.
	//
	// public int getPower() {
	// return int;
	// }
	// // Returns the current power setting.
	//
	// public int getSpeed() {
	// return int;
	// }
	// // Returns the current motor speed in degrees per second
	//
	// public int getStopAngle() {
	// return int;
	// }
	//
	// public int getTachoCount() {
	// return int;
	// }
	// // Returns the tachometer count.
	//
	// public boolean isMoving() {
	// return boolean;
	// }
	// // Returns true if the motor is in motion.
	//
	// public boolean isRegulating() {
	// return boolean;
	// }
	//
	// public boolean isRotating() {
	// return boolean;
	// }
	// // returns true when motor rotation task is not yet complete a specified
	// angle
	//
	// public void resetTachoCount() {
	// }
	// // Resets the tachometer count to zero.
}
