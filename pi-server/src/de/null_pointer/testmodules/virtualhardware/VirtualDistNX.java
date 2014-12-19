package de.null_pointer.testmodules.virtualhardware;

public class VirtualDistNX {

	private int biggestValueNoSpace = 800;
	private int smallestValueNoSpace = 100;
	private int biggestValueOK = 800;
	private int smallestValueOK = 100;
	private final int amountValues = 20;
	private int value[] = new int[amountValues];
	private String distNX[] = new String[amountValues];
	int range;
	

	/**
	 * @param maxValue must not be bigger than 800
	 * @param minValue has to be at least zero
	 */
	public VirtualDistNX(int maxValue, int minValue) {
		
		biggestValueNoSpace = maxValue;
		smallestValueNoSpace = minValue;
		smallestValueOK = biggestValueNoSpace + 1;
		
	}

	private void generateValue(int mode) {
		int additionOrSubtraction = (int) Math.random()*10;
		if(additionOrSubtraction>=5){
			
			if(mode == 0){
				range = biggestValueOK - smallestValueOK;
				value[0] = (int) Math.random() * range
						+ smallestValueOK;
				
				for (int i = 1; i < amountValues; i++) {

					value[i] = (int) Math.random() * 10 + value[i-1];
				}

				//Ueberlegung mit Grenzwertueberschreitung fortfuehren
				//entweder zweite Bedingung für Schleife oder ab bestimmtem Wert subtrahieren
			}else if(mode == 1){
				range = biggestValueNoSpace - smallestValueNoSpace;
				
				
				value[0] = (int) Math.random() * range
						+ smallestValueNoSpace;
				
				for (int i = 1; i < amountValues; i++) {
					
					value[i] = (int) Math.random() *10 + value[i-1];

				}
			}
			
		}else if(additionOrSubtraction<5){
			
			
			
		}
		
		
	}

	public String[] getOK() { // ok = mode 0
		generateValue(0);

		for (int i = 0; i < amountValues; i++) {

			distNX[i] = "*1;1;" + value[i] + ";0#";

		}

		return distNX;

	}

	public String[] getNoSpaceInFront() { // getNoSpaceInFront = mode 1
		generateValue(1);

		for (int i = 0; i < amountValues; i++) {

			distNX[i] = "*1;1;" + value[i] + ";0#";

		}

		return distNX;
	}

}
