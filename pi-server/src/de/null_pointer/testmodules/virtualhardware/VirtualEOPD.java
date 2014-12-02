package de.null_pointer.testmodules.virtualhardware;

public class VirtualEOPD {
	
	int eopdNumber;
	
	public VirtualEOPD(int eopd){
		
		eopdNumber = eopd;
		
	}
	
	
	public String[] getOK(){
		String test[] = {"*"+eopdNumber+";1;200;0#","*"+eopdNumber+";1;300;0#","*"+eopdNumber+";1;400;0#","*"+eopdNumber+";1;140;0#","*"+eopdNumber+";1;170;0#","*"+eopdNumber+";1;210;0#","*"+eopdNumber+";1;240;0#","*4;1;180;0#","*"+eopdNumber+";1;235;0#","*"+eopdNumber+";1;"
				+ "222;0#","*"+eopdNumber+";1;221;0#","*"+eopdNumber+";1;208;0#","*"+eopdNumber+";1;202;0#","*"+eopdNumber+";1;240;0#","*"+eopdNumber+";1;290;0#","*"+eopdNumber+";1;308;0#","*"+eopdNumber+";1;347;0#","*"+eopdNumber+";1;385;0#"};
		return test;
	}
	
	public String[] getNoSpaceRightOrLeft(){
		String test[] = {"*"+eopdNumber+";1;900;0#","*"+eopdNumber+";1;1000;0#","*"+eopdNumber+";1;980;0#","*"+eopdNumber+";1;970;0#","*"+eopdNumber+";1;995;0#","*"+eopdNumber+";1;960;0#","*"+eopdNumber+";1;988;0#","*"+eopdNumber+";1;999;0#","*"+eopdNumber+";1;900;0#","*"+eopdNumber+";1;1000;0#","*"+eopdNumber+";1;980;0#","*"+eopdNumber+";1;970;0#","*"+eopdNumber+";1;995;0#","*"+eopdNumber+";1;960;0#","*"+eopdNumber+";1;988;0#","*"+eopdNumber+";1;999;0#","*"+eopdNumber+";1;988;0#","*"+eopdNumber+";1;999;0#"};
		return test;
	}
	
	
	public String[] getIntersection(){
		String test[] = {"*"+eopdNumber+";1;900;0#","*"+eopdNumber+";1;1000;0#","*"+eopdNumber+";1;980;0#","*"+eopdNumber+";1;970;0#","*"+eopdNumber+";1;995;0#","*"+eopdNumber+";1;960;0#","*"+eopdNumber+";1;988;0#","*"+eopdNumber+";1;999;0#","*"+eopdNumber+";1;900;0#","*"+eopdNumber+";1;1000;0#","*"+eopdNumber+";1;980;0#","*"+eopdNumber+";1;970;0#","*"+eopdNumber+";1;995;0#","*"+eopdNumber+";1;960;0#","*"+eopdNumber+";1;988;0#","*"+eopdNumber+";1;999;0#","*"+eopdNumber+";1;988;0#","*"+eopdNumber+";1;999;0#"};
		return test;
	}	

}
