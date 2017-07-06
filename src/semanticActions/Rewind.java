package semanticActions;

import automaton.Lexer;

public class Rewind extends SimpleAction {

	public Rewind(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		
		// TODO Auto-generated method stub
		lexer.rewind();
		
		return null;
	}

}
