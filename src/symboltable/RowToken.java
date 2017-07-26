package symboltable;

public class RowToken extends Row{

	public RowToken(String token) {

		super(token);
		isReferencia=false;
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
