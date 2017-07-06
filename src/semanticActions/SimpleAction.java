package semanticActions;

import automaton.Lexer;

public abstract class SimpleAction extends SemanticAction{

	protected Lexer lexer;
	
	public SimpleAction(Lexer l){
		this.lexer=l;
	}
	
}
