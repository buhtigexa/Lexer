package semanticActions;

import automaton.Lexer;

public class Clear extends SimpleAction{

	
	
	public Clear(Lexer lexer) {
		super(lexer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		
		lexer.lexeme=new String();
		return null;
		
	}

}
