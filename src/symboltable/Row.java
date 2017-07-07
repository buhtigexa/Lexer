package symboltable;

public abstract class Row {

	protected String token;
	protected String lexeme;
	
	public Row(String token){
		
		this.token=token;
		
	}
	
	public String getToken(){
		
		return token;
		
	}
	
	public abstract String getLexeme();
	
	
}
