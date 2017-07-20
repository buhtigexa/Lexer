package codeGenerator;

public class Register {
	
	
	public 
	
	String name;
	boolean free;
	 boolean isExtended;
	
	public String getName() {
		if (isExtended){
			return "e"+name;
		}
		return name;
	}

	public void setName(String name) {
		
		this.name = name;
		this.free=true;
		this.isExtended=false;
	}

	public Register(String name){
		
		this.name = name;
		this.free=true;
		this.isExtended=false;
		
	}
	
	public void setBusy(){
		
		this.free=false;
		
	}
	
	public boolean isFree(){
		return free;
	}
	
	public void freeRegister(){
		free=true;
	}
	
	public void setExtended(boolean extend){
		this.isExtended=extend;
	}
	
	public void setExtended(){
		this.isExtended=true;
		
	}
}
