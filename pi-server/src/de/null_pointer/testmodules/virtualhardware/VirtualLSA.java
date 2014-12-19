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

	public VirtualLSA(int sensorN, int maxValueWhite, int minValueWhite, int maxValueBlack, int minValueBlack) {

		sensorNumber = sensorN;
		biggestValueWhite = maxValueWhite;
		smallestValueWhite = minValueWhite;
		biggestValueBlack = maxValueBlack;
		smallestValueBlack = minValueBlack;

	}

	private void generateValues(int mode) {
		int rangeWhite = biggestValueWhite - smallestValueWhite;
		int rangeBlack = biggestValueBlack - smallestValueBlack;
		int rangeBetValues = smallestValueWhite - biggestValueBlack;
		if (mode == 0) {

			for (int i = 0; i < amountValues; i++) {

				value[i] = (int) Math.random() * rangeWhite
						+ smallestValueWhite;

			}
		} else if(mode == 1){
			for (int i = 0; i < amountValues; i++) {

				value[i] = (int) Math.random() * rangeBlack
						+ smallestValueBlack;

			}

		}else if(mode == 2){
			
			for (int i = 0; i < amountValues; i++) {

				value[i] = (int) Math.random() * rangeBetValues
						+ biggestValueBlack;

			}
			
		}else{
			logger.error("mode > 2 - Invalid mode chosen!");
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
	
	public String[] getValuesBetweenWhiteAndBlack(){
		generateValues(2);
		
		for (int i = 0; i < amountValues; i++){
			LSA[i] = "*2;" + sensorNumber + ";" + value[i] + ";0#";
		}
		
		return LSA;
		
	}

}
