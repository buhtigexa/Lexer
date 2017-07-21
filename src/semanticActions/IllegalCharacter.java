package semanticActions;

import automaton.Lexer;

public class IllegalCharacter extends SimpleAction {

	public IllegalCharacter(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		String mensaje="[Error ] caracter ilegal " + c;
		Lexer.showError(mensaje);
		return null;
	}

}
