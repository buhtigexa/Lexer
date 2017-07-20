package codeGenerator;

public class Register {
	
	
	public 
	
	String name;
	boolean free;
	 boolean isExtended;
	
	public String getName() {
		if (isExtended){
			/*
			if ((name.compareTo("aux"))==0){
				System.out.println("Register : " + name);
			}
			*/
			return "e"+name;
		}
		return name;
	}

	public void setName(String name) {
		
		/*
		if ((name.compareTo("aux"))==0){
			System.out.println("Register : " + name);
		}
		*/
		
		this.name = name;
		this.free=true;
		this.isExtended=false;
	}

	public Register(String name){
		
		/*
		if ((name.compareTo("aux"))==0){
			System.out.println("Register : " + name);
		}
		*/
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
