package symboltable;

public class RowConst extends Row{

	
	
	
		
	public RowConst(String token) {
		super(token);

	}
	
	public RowConst(String token, String lexeme, String type){
		super(token);
		this.token=token;
		this.lexeme=lexeme;
		this.type=type;
	}

	@Override
	public String getLexeme() {
		// TODO Auto-generated method stub
		return lexeme;
	}
	
	public String getType() {
		return type;
	}

}


