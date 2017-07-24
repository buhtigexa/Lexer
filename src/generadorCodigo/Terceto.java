package generadorCodigo;

public class Terceto {

	public Object opD;
	public Object opI;
	public String operador;
	public int id;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Terceto(String operador,Object opI, Object opD){
	
		this.operador=operador;
		this.opD=opD;
		this.opI=opI;
		
	}
	
	
	public String toString(){
		
		String str="";
		String opIstr="";
		String opDstr="";
		if (opI!=null){
			opIstr="["+opI.toString()+"]";
		}
		if (opD !=null){
			opDstr="["+opD.toString()+"]";
		}
		if ( opI instanceof Terceto){
			opIstr="["+Integer.toString(((Terceto)opI).getId())+"]";
		}
		
		if ( opD instanceof Terceto){
			opDstr="["+Integer.toString(((Terceto)opD).getId())+"]";
		}
		
		str= "[ (" + operador  + ") "+ opIstr  + "   ---   "  + opDstr  + "  ]";
		return str;
	}
	
	
}
