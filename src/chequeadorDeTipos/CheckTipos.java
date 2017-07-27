package chequeadorDeTipos;

import symboltable.Row;
import generadorCodigo.Operando;
import generadorCodigo.Referencia;
import generadorCodigo.Terceto;

public class CheckTipos {

	public CheckTipos(){
		
	}
	
	
	public boolean isOk(Terceto t){
		
		/*
		Object opI = t.opI;
		Object opD = t.opD;
		
		String typeI=getType(opI);
		String typeD=getType(opD);
		
		if (typeI.compareToIgnoreCase(typeD)!=0){
			return false;
		}
		return true;
		
		*/
		
		Operando opI = (Operando) t.opI;
		Operando opD = (Operando) t.opD;
		
		if (opI.getType().equalsIgnoreCase(opD.getType())){
			return true;
		}
		return false;
	}
	
	/*
	protected String getType(Object o){
		
		String type="";
		
		if (o instanceof Terceto){
			type = ((Terceto)o).getType();
		}
		else if (o instanceof Referencia){
			type=((Referencia)o).getType();
		}
		else if (o instanceof Row){
			type=((Row)o).getType();
		}
		return type;
	}
	*/
}
