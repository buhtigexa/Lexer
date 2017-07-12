package semanticActions;

import symboltable.RowIdentifier;
import symboltable.RowToken;
import automaton.Lexer;

public class ID extends SimpleAction{

	public ID(Lexer l) {
		super(l);

	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		
		Object token=null;
		
		
		
		if (lexer.isReservedWord(lexer.lexeme)){
				
				System.out.println(" Palabra reservada :  "  + lexer.lexeme);
				token=new RowToken(lexer.lexeme);
				return token;
		}
		else
			if (lexer.lexeme.length()> 15){
				System.out.println("Identificador: " + lexer.lexeme  + "  muy largo , se trunca a: " + lexer.lexeme.substring(0, 15));
				lexer.warnings++;
				lexer.lexeme=lexer.lexeme.substring(0, 15);
				}
			token = new RowIdentifier("T_IDENTIFIER",lexer.lexeme,"");
			
		//System.out.println(token);	
		return token;
	}

}

/*

T_STRING
T_RW_IF
T_RW_ELSE
T_RW_DO
T_RW_PRINT
T_RW_WHILE
T_RW_TOLONG
T_RW_LONG
T_RW_UINT

*/