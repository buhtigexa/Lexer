package semanticActions;

import automaton.Lexer;

public class TComment extends SimpleAction{

	public TComment(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		System.out.println("[COMMENT :] "+lexer.lexeme);
		return null;
	}

}
