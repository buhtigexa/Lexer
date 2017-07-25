package generadorCodigo;

import java.util.ArrayList;
import java.util.Stack;

import chequeadorDeTipos.CheckTipos;

import automaton.Lexer;



public class GeneradorTercetos {

	public static ArrayList<Terceto> tercetos;
	public Lexer lexer;
	public Stack<Object> pila;
	
	public GeneradorTercetos(Lexer l){
		
		this.lexer=l;
		tercetos = new ArrayList<Terceto>();
		pila=new Stack<Object>();
		
	}
	
	
	
	
	
	public void crearTerceto(String operador, Operando opI, Operando opD){
		
		
		Terceto terceto = new Terceto(operador,opI,opD);
	
		tercetos.add(terceto);
	}
	
	
	public void add(Terceto t){
		
		t.setId(tercetos.size());
		tercetos.add(t);
		System.out.println("...................................................................................................");
		System.out.println(" TERCETO :"  +t.getId()  + "  "  + t   +  "Asignacion :"  +  t.isAsignacion()  +  ""   + t.opI.getClass()  + " "  +  t.opD.getClass()  +  "\n");  
		
	}
	
	protected void checkearTipos(Terceto t) {
		
		Operando opI=(Operando) t.opI;
		Operando opD=(Operando) t.opD;
		
		System.out.println("TERCETO NRO : " + t.getId()   + " OPERANDOS :  "  +  opI.getClass()  +  "     "   + opD.getClass() );

		try { 
		
		if (opI.getType().compareToIgnoreCase(opD.getType())!=0){
			Lexer.showError("Tipos incompatibles . Se requiere conversión explícita");
		}
		else
			t.setType(opI.getType());
		}
		catch( ClassCastException e){
			e.printStackTrace();
		}
	}





	public int getNextTercetoId(){
		
		// La cantidad de tercetos contada desde cero . Pero también el número de terceto siguiente.
		
		return tercetos.size();
	}
	
	public void apilar(Object t){
		
		pila.push(t);
	}
	
	public Object desapilar(){
		
		return pila.pop();
	}
	

	public void completarTerceto(Terceto t,Object op){
		
		t.opI=op;
	}
	
	public Terceto getTerceto(int id){
		
	
		return tercetos.get(id);
		
	}
	
	public String toString(){
		
		String str="";
		for (Terceto t: tercetos){
			str+="\n  " + t.getId() + ") " + t.toString();
		}
		return str;
	}

}
