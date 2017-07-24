package generadorCodigo;

import symboltable.Row;

public class Operando {
	
	public Row row;
	public boolean isReferencia;
	public int referencia;

	public Operando(Row row,boolean isReferencia,int referencia){
		
		this.row=row;
		this.isReferencia=isReferencia;
		this.referencia=referencia;
		
	}
	
	public String toString(){
		
		String str="";
		
		if (isReferencia){
			str=Integer.toString(referencia);
		}
		else
			str=" <<  "  + row.getToken()  + "  "  + row.getLexeme()  +  "  "  + "  "  + row.getType();
		
		return str;
		
		
	}

}
