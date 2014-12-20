package de.null_pointer.testmodules.virtualhardware;

public class VirtualEOPD {
	
	private int eopdNumber;
	
	public VirtualEOPD(int eopdN){
		
		eopdNumber = eopdN;
		
	}
	
	
	public String[] getOK(){
		String eopd[] = {"*"+eopdNumber+";1;200;0#","*"+eopdNumber+";1;300;0#","*"+eopdNumber+";1;400;0#","*"+eopdNumber+";1;140;0#","*"+eopdNumber+";1;170;0#","*"+eopdNumber+";1;210;0#","*"+eopdNumber+";1;240;0#","*"+eopdNumber+";1;180;0#","*"+eopdNumber+";1;235;0#","*"+eopdNumber+";1;222;0#","*"+eopdNumber+";1;221;0#","*"+eopdNumber+";1;208;0#","*"+eopdNumber+";1;202;0#","*"+eopdNumber+";1;240;0#","*"+eopdNumber+";1;290;0#","*"+eopdNumber+";1;308;0#","*"+eopdNumber+";1;347;0#","*"+eopdNumber+";1;385;0#"};
		return eopd;
	}
	
	public String[] getNoSpaceRightOrLeft(){
		String eopd[] = {"*"+eopdNumber+";1;900;0#","*"+eopdNumber+";1;1000;0#","*"+eopdNumber+";1;980;0#","*"+eopdNumber+";1;970;0#","*"+eopdNumber+";1;995;0#","*"+eopdNumber+";1;960;0#","*"+eopdNumber+";1;988;0#","*"+eopdNumber+";1;999;0#","*"+eopdNumber+";1;900;0#","*"+eopdNumber+";1;1000;0#","*"+eopdNumber+";1;980;0#","*"+eopdNumber+";1;970;0#","*"+eopdNumber+";1;995;0#","*"+eopdNumber+";1;960;0#","*"+eopdNumber+";1;988;0#","*"+eopdNumber+";1;999;0#","*"+eopdNumber+";1;988;0#","*"+eopdNumber+";1;999;0#"};
		return eopd;
	}
	
	
	public String[] getIntersection(){
		String eopd[] = {"*"+eopdNumber+";1;100;0#","*"+eopdNumber+";1;80;0#","*"+eopdNumber+";1;46;0#","*"+eopdNumber+";1;30;0#","*"+eopdNumber+";1;20;0#","*"+eopdNumber+";1;10;0#","*"+eopdNumber+";1;5;0#","*"+eopdNumber+";1;6;0#","*"+eopdNumber+";1;10;0#","*"+eopdNumber+";1;4;0#","*"+eopdNumber+";1;2;0#","*"+eopdNumber+";1;3;0#","*"+eopdNumber+";1;9;0#","*"+eopdNumber+";1;8;0#","*"+eopdNumber+";1;11;0#","*"+eopdNumber+";1;12;0#","*"+eopdNumber+";1;14;0#","*"+eopdNumber+";1;16;0#"};
		return eopd;
	}	
	
	public String[] getCorrecting(){
		String eopd[] = {""};
		return eopd;
	}

}
