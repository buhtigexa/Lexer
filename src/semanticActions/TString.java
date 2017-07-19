package semanticActions;

import symboltable.RowIdentifier;
import automaton.Lexer;

public class TString extends SimpleAction{

	public TString(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		
		Object token=null;
		
		if (!lexer.symTable.contains(lexer.lexeme)){
			token=new RowIdentifier("string", lexer.lexeme, "string");
			lexer.symTable.add(token);
		}
		else
			token=lexer.symTable.getRow(lexer.lexeme);
		
		return token;
		
	
	}

}
