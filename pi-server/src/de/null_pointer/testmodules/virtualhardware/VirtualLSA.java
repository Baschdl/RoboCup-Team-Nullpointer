package de.null_pointer.testmodules.virtualhardware;

import org.apache.log4j.Logger;

public class VirtualLSA {
	private static Logger logger = Logger.getLogger(VirtualLSA.class);
	private int sensorNumber;
	private final int amountValues = 20;
	private String LSA[] = new String[amountValues];
	private int value[] = new int[amountValues];
	private int biggestValueWhite = 101;
	private int smallestValueWhite = 93;
	private int biggestValueBlack = 6;
	private int smallestValueBlack = 0;

	/**
	 * 
	 * @param sensorN
	 *            sensorNumber of the sensor of the LightSensorArray
	 * @param maxValueWhite
	 *            must not be bigger than 100
	 * @param minValueWhite
	 *            must not be smaller than maxValueBlack + 1
	 * @param maxValueBlack
	 *            must not be bigger than minValueWhite -1
	 * @param minValueBlack
	 *            must not be smaller than 0
	 */
	public VirtualLSA(int sensorN, int maxValueWhite, int minValueWhite,
			int maxValueBlack, int minValueBlack) {

		sensorNumber = sensorN;
		biggestValueWhite = maxValueWhite;
		smallestValueWhite = minValueWhite;
		biggestValueBlack = maxValueBlack;
		smallestValueBlack = minValueBlack;

	}

	/**
	 * 
	 * @param mode
	 *            to generate white values --> mode = 0; to generate black
	 *            values --> mode = 1; to generate values between black and
	 *            white --> mode = 2
	 */

	private void generateValues(int mode) {
		int additionOrSubtraction = (int) Math.random() * 10;
		int rangeWhite = biggestValueWhite - smallestValueWhite;
		int rangeBlack = biggestValueBlack - smallestValueBlack;
		int rangeBetValues = smallestValueWhite - biggestValueBlack;
		if (mode == 0) {
			value[0] = (int) Math.random() * rangeWhite + smallestValueWhite;
		} else if (mode == 1) {
			value[0] = (int) Math.random() * rangeBlack + smallestValueBlack;
		} else if (mode == 2) {

			value[0] = (int) Math.random() * rangeBetValues + biggestValueBlack;
		} else {
			logger.error("mode > 2 - Invalid mode chosen!");
		}

		for (int i = 1; i < amountValues; i++) {

			if (additionOrSubtraction >= 5) {// addition
				if (mode == 0) {

					if (value[i - 1] >= biggestValueBlack - 3) {
						additionOrSubtraction = 1;
					} else {
						value[i] = (int) Math.random() * 10 / 3 + value[i - 1];
					}

				} else if (mode == 1) {
					if (value[i - 1] >= biggestValueWhite - 3) {
						additionOrSubtraction = 1;
					} else {

						value[i] = (int) Math.random() * 10 / 3 + value[i - 1];

					}
				} else if (mode == 2) {
					if (value[i - 1] >= smallestValueWhite - 3) {
						additionOrSubtraction = 1;
					} else {

						value[i] = (int) Math.random() * 10 / 3 + value[i - 1];
					}

				}

			} else if (additionOrSubtraction < 5) { // subtraction
				if (mode == 0) {
					if (value[i - 1] < smallestValueBlack + 3) {
						additionOrSubtraction = 9;
					} else {
						value[i] = value[i - 1] - (int) Math.random() * 10 / 3;
					}

				} else if (mode == 1) {
					if (value[i - 1] < smallestValueWhite + 3) {
						additionOrSubtraction = 9;
					} else {
						value[i] = value[i - 1] - (int) Math.random() * 10 / 3;
					}
				} else if (mode == 2) {
					if (value[i - 1] < biggestValueBlack + 3) {
						additionOrSubtraction = 9;
					} else {
						value[i] = value[i - 1] - (int) Math.random() * 10 / 3;
					}
				}
			}
		}

	}

	public String[] getBlack() {
		generateValues(0);

		for (int i = 0; i < amountValues; i++) {
			LSA[i] = "*2;" + sensorNumber + ";" + value[i] + ";0#";
		}
		return LSA;
	}

	public String[] getWhite() {
		generateValues(1);

		for (int i = 0; i < amountValues; i++) {
			LSA[i] = "*2;" + sensorNumber + ";" + value[i] + ";0#";
		}
		return LSA;

	}

	public String[] getValuesBetweenWhiteAndBlack() {
		generateValues(2);

		for (int i = 0; i < amountValues; i++) {
			LSA[i] = "*2;" + sensorNumber + ";" + value[i] + ";0#";
		}

		return LSA;

	}

}
