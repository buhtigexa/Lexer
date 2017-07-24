package generadorCodigo;

public class Terceto {

	public Operando opD,opI;
	public String operador;
	
	
	public Terceto(String operador,Operando opI, Operando opD){
	
		this.operador=operador;
		this.opD=opD;
		this.opI=opI;
		
	}
	
	
	public String toString(){
		
		String str="";
		String opI="";
		
		str= "[" + operador + "  ---  " +  opI  + "   ---   "  + opD + "  ]";
		return str;
	}
	
	
}
