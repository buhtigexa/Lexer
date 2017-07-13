package semanticActions;

import symboltable.RowConst;
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
		
		if ((constant <= 65535)&&(constant >=0)){
			token = new RowConst("const",lexer.lexeme,"UINT");
		}
		
		else 
			if ( (constant > 65536) && ((constant-1) <= 2147483647)){
				//System.out.println(" CONSTANT LONG :"  + constant);
				token = new RowConst("const",lexer.lexeme,"LONG");
			}
		
			else {
				
				System.out.println("[Error - Constante fuera de rango ] " + lexer.lexeme);
				lexer.errors++;
			}
				
		//System.out.println(token);
		return token;
	}

}
