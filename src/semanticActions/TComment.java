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
		String mensaje="[COMMENT :] "+lexer.lexeme;
		Lexer.showMessage(mensaje);
		return null;
	}

}
