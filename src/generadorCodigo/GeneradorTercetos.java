package generadorCodigo;

import java.util.ArrayList;
import java.util.Stack;

import chequeadorDeTipos.CheckTipos;

import automaton.Lexer;



public class GeneradorTercetos {

	public static ArrayList<Terceto> tercetos;
	public Lexer lexer;
	public Stack<Object> pila;
	public CheckTipos fijaTipos;
	
	public GeneradorTercetos(Lexer l){
		
		this.lexer=l;
		tercetos = new ArrayList<Terceto>();
		pila=new Stack<Object>();
		fijaTipos=new CheckTipos();
	}
	
	
	
	
	
	public void crearTerceto(String operador, Operando opI, Operando opD){
		
		
		Terceto terceto = new Terceto(operador,opI,opD);
	
		tercetos.add(terceto);
	}
	
	
	public void add(Terceto t){
		
		t.setId(tercetos.size());
		tercetos.add(t);
		
		try {
			if (fijaTipos.isOk(t)){
				t.setType(((Operando)t.opD).getType());
			}
			else{
					Lexer.showError("Tipos diferentes . Se requiere conversión explícita ");
			}
		}
		catch(NullPointerException e){
			System.out.println(t);
			e.printStackTrace();
		}
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
