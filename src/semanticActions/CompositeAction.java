package semanticActions;

import automaton.Lexer;

public abstract class CompositeAction extends SemanticAction{

	
	public Lexer lexer;
	public SemanticAction sa1,sa2;
	
	
	public CompositeAction(Lexer lexer,SemanticAction sa1,SemanticAction sa2){
		
		this.lexer=lexer;
		this.sa1=sa1;
		this.sa2=sa2;
		
	}
}
