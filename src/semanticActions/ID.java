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
			token = new RowIdentifier("ID",lexer.lexeme,"");
			
			
		return token;
	}

}
