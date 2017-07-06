package semanticActions;

import automaton.Lexer;

public class Constant extends SimpleAction{

	public Constant(Lexer l) {
		super(l);
	
		
	}

	@Override
	public Object execute(char c) {
		
		Object token = null;
		long constant=0;
		
		constant=Long.parseLong(lexer.lexeme);
		
		if (constant <= 65535){
			//token ( uint, ptr_ts)
			System.out.println(" CONSTANT UINT:"  + constant);
			token=null;
		}
		else 
			if ( (constant > 65566) && ((constant-1) <= 2147483647)){
				//token (long, ptr_ts)
				System.out.println(" CONSTANT LONG :"  + constant);
				token=null;
			}
		
			else {
				System.out.println("[Error - Constante fuera de rango ] " + lexer.lexeme);
				lexer.errors++;
			}
				
		
		return null;
	}

}
