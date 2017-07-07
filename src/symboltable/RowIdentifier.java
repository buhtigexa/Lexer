package symboltable;

public class RowIdentifier extends Row{

	
	
	public String getType() {
		return type;
	}

	public RowIdentifier(String token) {
		super(token);
		type="";
	
	}
	
	public RowIdentifier(String token, String lexeme,String type){
		
		super(token);
		this.lexeme=lexeme;
		this.type=type;
	}

	@Override
	public String getLexeme() {
		
		return lexeme;
	}

}
