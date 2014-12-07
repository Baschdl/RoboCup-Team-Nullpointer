package de.null_pointer.testmodules.virtualhardware;

public class VirtualLSA {
	private int sensorNumber;
	
	public VirtualLSA(int sensorN){
		
		sensorNumber = sensorN;
		
	}

	public String[] getWhite(){
		String LSA1[] = {"*1;1;200;0#","*1;1;300;0#","*1;1;400;0#","*1;1;140;0#","*1;1;170;0#","*1;1;210;0#","*1;1;240;0#","*1;1;180;0#","*1;1;235;0#","*1;1;"
				+ "222;0#","*1;1;221;0#","*1;1;208;0#","*1;1;202;0#","*1;1;240;0#","*1;1;290;0#","*1;1;308;0#","*1;1;347;0#","*1;1;385;0#"};
		return LSA1;
	}
	
	public String[] getAllBlack(){
		
		String LSA[][] = {{"*1;1;900;0#","*1;1;1000;0#","*1;1;980;0#","*1;1;970;0#","*1;1;995;0#","*1;1;960;0#","*1;1;988;0#","*1;1;999;0#","*1;1;900;0#","*1;1;1000;0#","*1;1;980;0#","*1;1;970;0#","*1;1;995;0#","*1;1;960;0#","*1;1;988;0#","*1;1;999;0#","*1;1;988;0#","*1;1;999;0#"},{"*2;1;900;0#","*2;1;1000;0#","*2;1;980;0#","*2;1;970;0#","*2;1;995;0#","*2;1;960;0#","*2;1;988;0#","*2;1;999;0#","*2;1;900;0#","*2;1;1000;0#","*2;1;980;0#","*2;1;970;0#","*2;1;995;0#","*2;1;960;0#","*2;1;988;0#","*2;1;999;0#","*2;1;988;0#","*2;1;999;0#"},{"*3;1;900;0#","*3;1;1000;0#","*3;1;980;0#","*3;1;970;0#","*3;1;995;0#","*3;1;960;0#","*3;1;988;0#","*3;1;999;0#","*3;1;900;0#","*3;1;1000;0#","*3;1;980;0#","*3;1;970;0#","*3;1;995;0#","*3;1;960;0#","*3;1;988;0#","*3;1;999;0#","*3;1;988;0#","*3;1;999;0#"},{"*4;1;900;0#","*4;1;1000;0#","*4;1;980;0#","*4;1;970;0#","*4;1;995;0#","*4;1;960;0#","*4;1;988;0#","*4;1;999;0#","*4;1;900;0#","*4;1;1000;0#","*4;1;980;0#","*4;1;970;0#","*4;1;995;0#","*4;1;960;0#","*4;1;988;0#","*4;1;999;0#","*4;1;988;0#","*4;1;999;0#"},{"*5;1;900;0#","*5;1;1000;0#","*5;1;980;0#","*5;1;970;0#","*5;1;995;0#","*5;1;960;0#","*5;1;988;0#","*5;1;999;0#","*5;1;900;0#","*5;1;1000;0#","*5;1;980;0#","*5;1;970;0#","*5;1;995;0#","*5;1;960;0#","*5;1;988;0#","*5;1;999;0#","*5;1;988;0#","*5;1;999;0#"},{"*6;1;900;0#","*6;1;1000;0#","*6;1;980;0#","*6;1;970;0#","*6;1;995;0#","*6;1;960;0#","*6;1;988;0#","*6;1;999;0#","*6;1;900;0#","*6;1;1000;0#","*6;1;980;0#","*6;1;970;0#","*6;1;995;0#","*6;1;960;0#","*6;1;988;0#","*6;1;999;0#","*6;1;988;0#","*6;1;999;0#"},{"*7;1;900;0#","*7;1;1000;0#","*7;1;980;0#","*7;1;970;0#","*7;1;995;0#","*7;1;960;0#","*7;1;988;0#","*7;1;999;0#","*7;1;900;0#","*7;1;1000;0#","*7;1;980;0#","*7;1;970;0#","*7;1;995;0#","*7;1;960;0#","*7;1;988;0#","*7;1;999;0#","*7;1;988;0#","*7;1;999;0#"},{"*8;1;900;0#","*8;1;1000;0#","*8;1;980;0#","*8;1;970;0#","*8;1;995;0#","*8;1;960;0#","*8;1;988;0#","*8;1;999;0#","*8;1;900;0#","*8;1;1000;0#","*8;1;980;0#","*8;1;970;0#","*8;1;995;0#","*8;1;960;0#","*8;1;988;0#","*8;1;999;0#","*8;1;988;0#","*8;1;999;0#"}};
		return LSA[sensorNumber]; 
		/*
		 * TODO Set values in getAllBlack and set values + sensorNumber in getWhite
		 */
	}
	
}
