package generadorAssembler;

public class Register {
	
	public boolean free;
	public String name;
	public boolean extended;
	
	public Register(String name){
		extended=false;
		free=true;
		this.name=name;
		
	}
	public boolean isFree() {
		// TODO Auto-generated method stub
		return free;
	}

	public void setBusy() {
		// TODO Auto-generated method stub
		free=false;
	}

	public String getName() {
		// TODO Auto-generated method stub
		if (isExtended()){
			return "e"+name;
		}
		
		return name;
	}

	private boolean isExtended() {
		// TODO Auto-generated method stub
		return extended;
	}
	
	public void freeRegister() {
		// TODO Auto-generated method stub
		free=true;
	}
	public void setExtended() {
		// TODO Auto-generated method stub
		extended=true;
	}
	public void setExtended(boolean b) {
		// TODO Auto-generated method stub
		extended=b;
	}
	

	
}
