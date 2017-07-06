package semanticActions;

import automaton.Lexer;

public class ID extends SimpleAction{

	public ID(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		
		String token;
		
		if (lexer.lexeme.length()>=15){
			System.out.println("Identificador: " + lexer.lexeme  + "  muy largo , se trunca a: " + lexer.lexeme.substring(0, 15));
			lexer.warnings++;
		}
		System.out.println("Identificador detectado:	" + lexer.lexeme);
		
		if (lexer.isReservedWord(lexer.lexeme)){
			System.out.println(" Palabra reservada :  "  + lexer.lexeme);
			token=lexer.lexeme;
			return token;
		}
		else
			token=null;
		
		return token;
	}

}
