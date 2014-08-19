package de.null_pointer.sensorprocessing_pi;

public class LSAProcessingPi {
	
	private int black = -1;
	private int white = -1;
	private int[] lsa = new int[]{-1,-1,-1,-1,-1,-1,-1,-1};
	private boolean[] blackWhite= new boolean[8];
	
	public LSAProcessingPi(){}
	
	public void setBlack(int black){
		this.black = black;
	}
	
	public int getBlack(){
		return black;
	}
	
	public void setWhite(int white){
		this.white = white;
	}
	
	public int getWhite(){
		return white;
	}
	
	public void setLSA(int index, int value){
		lsa[index] = value;
	}
	
	public int getLSA(int index){
		return lsa[index];
	}
	
	public int[] getLSA(){
		return lsa;
	}
}
