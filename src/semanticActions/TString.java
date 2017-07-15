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
		
		return new RowIdentifier("string", lexer.lexeme, "STRING");
	}

}
