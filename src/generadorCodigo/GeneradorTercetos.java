package generadorCodigo;

import java.util.ArrayList;



public class GeneradorTercetos {

	ArrayList<Terceto> tercetos;

	
	public GeneradorTercetos(){
		
		tercetos = new ArrayList<Terceto>();
	}
	public void crearTerceto(String operador, Operando opI, Operando opD){
		
		
		Terceto terceto = new Terceto(operador,opI,opD);
		tercetos.add(terceto);
	}
	
	
	public void add(Terceto t){
		
		tercetos.add(t);
	}
	

	public String toString(){
		
		String str="";
		for (Terceto t: tercetos){
			str+="\n  " + t.toString();
		}
		return str;
	}

}
