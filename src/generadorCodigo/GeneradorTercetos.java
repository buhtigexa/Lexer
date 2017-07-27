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
		update(t);
		checkearTipos(t);
	
	}
	
	protected void checkearTipos(Terceto t) {
		
		Operando opI = (Operando)t.opI;
		Operando opD = (Operando)t.opD;
		
		String typeI="";
		String typeD="";
		
		if (!opI.isReferencia())
			typeI=opI.getType();
		
		if (!opD.isReferencia())
			typeD=opD.getType();
		
		if (opI.isReferencia() && !(t.isSalto())){
			Terceto referenciado=tercetos.get(((Referencia)opI).nroTerceto);
			typeI=referenciado.getType();
		}
		
		if (opD.isReferencia() && !(t.isSalto()) ){
			Terceto referenciado=tercetos.get(((Referencia)opD).nroTerceto);
			typeD=referenciado.getType();
		}
		
		if (t.operador.compareTo("tolong")==0){
			t.setType("long");
			return;
		}
		
		if (typeD.compareTo(typeI)==0){
			t.setType(typeI);
		}
		else
			Lexer.showError("Error de tipos, se requiere conversión explícita");
		
	}

	
	public void updateAllReferences(){
		
		for (Terceto t: tercetos){
			update(t);
		}
	}
	
	protected void update(Terceto terceto){
		
		Referencia ref =null;
		Terceto referenciado =null;
		if (((Operando)terceto.opI).isReferencia){
			ref = (Referencia)terceto.opI;
			referenciado =tercetos.get(ref.nroTerceto);
			if (referenciado.isAsignacion){
				//System.out.println(" Rerencia --->" + referenciado.opI);
				terceto.opI =(Operando) referenciado.opI;
				
			}
			
		}
			if (((Operando)terceto.opD).isReferencia){
				ref=(Referencia)terceto.opD;
				referenciado=tercetos.get(ref.nroTerceto);
				if (referenciado.isAsignacion()){
					terceto.opD=(Operando)referenciado.opI;
				}
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
	
	
	public void etiquetador(String finalLabel){
	
		Terceto src=null;
		int posTercetoAnterior=-1;
		Terceto tercetoAnterior=null;
		String operadorAnterior="";
		String label="";
		Terceto tercetoDestino=null;
		
		for (int i=0; i < tercetos.size() ;i++){
			src=tercetos.get(i);
			posTercetoAnterior=i-1;
			if (src!=null) {
				if (posTercetoAnterior >=0){
					tercetoAnterior=tercetos.get(posTercetoAnterior);
					operadorAnterior=tercetoAnterior.operador;
					src.operadorAnterior=operadorAnterior;
					
				}
				if (src.operador.compareTo("BF")==0 || src.operador.compareTo("BI")==0){
					Referencia salto = (Referencia) src.opI;
					int destino=salto.nroTerceto;
					if (src.operador.compareTo("BF")==0){
						src.type=tercetos.get(posTercetoAnterior).type;
					}
						else
						src.type=tercetos.get(destino).type;
					
					if (destino < tercetos.size()){
						tercetoDestino=tercetos.get(destino);
						label=getNewLabel(destino);
						src.labelSrc=label;
						tercetoDestino.labelDst=label;
					}
					else
						src.labelSrc=finalLabel;
						}
				}
		}
	}
	
	
	public String getNewLabel(int index){

        String noJmp=Integer.toString(index);
        String temp = "label"+noJmp;
        return temp;


}
}