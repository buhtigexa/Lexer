package generadorCodigo;

import java.util.ArrayList;
import java.util.Stack;

import automaton.Lexer;



public class GeneradorTercetos {

	public ArrayList<Terceto> tercetos;
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
		
	}
	
	public int getNextTercetoId(){
		
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
		
		Terceto t=null;
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
