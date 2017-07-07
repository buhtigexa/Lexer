package symboltable;

public class RowToken extends Row{

	public RowToken(String token) {

		super(token);
	
	}

	@Override
	public String getLexeme() {
	
		return token;
	
	}

	@Override
	public String getType() {
	
		return "";
	
	}


}
