package de.null_pointer.motorcontrol_pi;

public class MotorControlPi {

	/**
	 * Methode zum vorwaerts Fahren des Roboters.
	 * 
	 * @param speed
	 *            Geschwindigkeit des Fahrens
	 */
	// synchronized noetig?
	public void forward(int speed) {

	}

	/**
	 * Methode zum rueckwaerts Fahren des Roboters.
	 * 
	 * @param speed
	 *            Geschwindigkeit des Fahrens
	 */
	// synchronized noetig?
	public void backward(int speed) {

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
	public void rightturn(int speed, int difference) {

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
	public void leftturn(int speed, int difference) {

	}

	/**
	 * Methode zum Rotieren des Roboters auf der Stelle nach rechts.
	 * 
	 * @param angle Winkel der Rotation
	 */
	public void rotateright(int angle) {

	}

	/**
	 * Methode zum Rotieren des Roboters auf der Stelle nach links.
	 * 
	 * @param angle angle Winkel der Rotation
	 */
	public void rotateleft(int angle) {

	}

	/**
	 * Methode zum Stoppen der Motoren, sodass sich die Raeder nicht mehr weiter drehen
	 */
	public void stop() {

	}

	/**
	 * Methode zum weichen Abbremsen der Motoren, die Raeder "rollen" aus
	 */
	public void flt() {

	}

}
