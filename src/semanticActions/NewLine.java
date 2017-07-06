package semanticActions;

import automaton.Lexer;

public class NewLine extends SimpleAction{

	public NewLine(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		lexer.line++;
		return null;
	}

}
