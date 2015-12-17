package de.null_pointer.testmodules.virtualhardware;

public class VirtualAbsIMUACG {
	
	private final int amountValues = 20;
	private String[] absIMU = new String[amountValues];
	private int[] value = new int[amountValues];
	
	public VirtualAbsIMUACG(){
		
	}
	
	private void generateValues(){
		
	}
	
	public String[] getDrivingForward(){
		
		return absIMU;
		
	}
	
	public String[] getRightTurn(){
		return absIMU;
	}
	
	public String[] getLeftTurn(){
		return absIMU;
	}
	
	public String[] get180DegreeTurn(){
		return absIMU;
	}
	
	public String[] getCorrecting(){
		return absIMU;
	}
	
	public String[] getRamp(){
		return absIMU;
	}
	
	public String[] getObstacle(){
		return absIMU;
	}
	
}
