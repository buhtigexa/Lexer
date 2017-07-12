package symboltable;

public abstract class Row {

	protected String token;
	protected String lexeme;
	public String type;
	
	public Row(String token){
		this.token=token;
	}
	
	public String getToken(){
		return token;
	}
	
	public abstract String getLexeme();
	public abstract String getType();
	
	public String toString(){
		
		return " TOKEN: " + token + " LEXEME: " + lexeme + " TYPE: " + type;
		
	}
	
	
}
