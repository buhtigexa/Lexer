package symboltable;

import generadorCodigo.Operando;

public abstract class Row extends Operando{

	protected String token;
	protected String lexeme;
	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}

	public String type;
	
	public void setType(String type) {
		this.type = type;
	}

	public Row(String token){
		this.token=token;
	}
	
	public String getToken(){
		return token;
	}
	
	public abstract String getLexeme();
	public abstract String getType();
	
	public String toString(){
		
		return "< TOKEN: " + token + " LEXEME: " + lexeme + " TYPE: " + type  + ">";
		//return "[ " + lexeme  + " ]";
	}
	
	public boolean isEmptyType(){
		 
		 if ( type=="" )
		        return true;
		 return false;
	}
}
