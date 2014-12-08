package de.null_pointer.testmodules.virtualhardware;

public class VirtualLSA {
	private int sensorNumber;
	private String LSA[] = new String[8];
	private int value[] = new int[8];
	private int biggestValueWhite = 101;
	private int smallestValueWhite = 93;
	private int biggestValueBlack = 6;
	private int smallestValueBlack = 0;

	public VirtualLSA(int sensorN) {

		sensorNumber = sensorN;

	}

	private void generateValue(boolean black) {
		int rangeWhite = biggestValueWhite - smallestValueWhite;
		int rangeBlack = biggestValueBlack - smallestValueBlack;
		if (black == false) {

			for (int i = 0; i < 8; i++) {

				value[i] = (int) Math.random() * rangeWhite
						+ smallestValueWhite;

			}
		} else {
			for (int i = 0; i < 8; i++) {

				value[i] = (int) Math.random() * rangeBlack
						+ smallestValueBlack;

			}

		}

	}

	public String[] getWhite() {
		generateValue(false);

		for (int i = 0; i < 8; i++) {
			LSA[i] = "*2;" + sensorNumber + value[i] + ";0#";
		}
		return LSA;
	}

	public String[] getAllBlack() {
		generateValue(true);

		for (int i = 0; i < 8; i++) {
			LSA[i] = "*2;" + sensorNumber + value[i] + ";0#";
		}
		return LSA;

	}

}
