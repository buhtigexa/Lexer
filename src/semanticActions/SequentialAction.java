package semanticActions;

import automaton.Lexer;

public class SequentialAction extends CompositeAction{

	public SequentialAction(Lexer lexer, SemanticAction sa1, SemanticAction sa2) {
		super(lexer, sa1, sa2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		sa1.execute(c);
		
		return sa2.execute(c);
		
		//return null;
	}

}
