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
		System.out.println("[Error ] caracter ilegal " + c);
		lexer.errors++;
		return null;
	}

}
