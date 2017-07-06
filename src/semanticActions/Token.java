package semanticActions;

import automaton.Lexer;

public class Token extends SimpleAction {

	public Token(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		System.out.println(" TOKEN : " + lexer.lexeme);
		return null;
	}

}
