package semanticActions;

import symboltable.RowToken;
import automaton.Lexer;

public class Token extends SimpleAction {

	public Token(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		Object token = null;
		
		token = new RowToken(lexer.lexeme);
		
		return token;
		
	}

}
