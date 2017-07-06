package semanticActions;

import automaton.Lexer;

public class Accumulate extends SimpleAction{

	
	public Accumulate(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		
		this.lexer.lexeme+=c;
		this.lexer.column++;
		return null;
	
	}

}
