package generadorCodigo;

import java.util.ArrayList;

import automaton.Lexer;



public class GeneradorTercetos {

	public ArrayList<Terceto> tercetos;
	public Lexer lexer;
	
	public GeneradorTercetos(Lexer l){
		
		this.lexer=l;
		tercetos = new ArrayList<Terceto>();
	
	}
	
	public void crearTerceto(String operador, Operando opI, Operando opD){
		
		
		Terceto terceto = new Terceto(operador,opI,opD);
		tercetos.add(terceto);
	}
	
	
	public void add(Terceto t){
		
		t.setId(tercetos.size());
		tercetos.add(t);
		

		
	}
	

	public String toString(){
		
		String str="";
		for (Terceto t: tercetos){
			str+="\n  " + t.getId() + ") " + t.toString();
		}
		return str;
	}

}
