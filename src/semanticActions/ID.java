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
		
		Object token=null;
		
		if (lexer.isReservedWord(lexer.lexeme)){
			token = new RowToken(lexer.lexeme);
		}
		else{
			
			// si es identificador, truncar si es largo .
			// aplicar namemangling : si no está en TS ,agregar.
			// 						: si está en TS, retornar.
			if (lexer.lexeme.length()>15){
				lexer.warnings++;
				System.out.println("Identificador muy largo , se trunca : " +  lexer.lexeme.substring(0, 15));
				lexer.lexeme=lexer.lexeme.substring(0, 15);
				}
				lexer.lexeme=lexer.symTable.getDecorator().decorate(lexer.lexeme);
				//System.out.println("[ LEXER - name mangling applied :] " +  lexer.lexeme);
				if (!lexer.symTable.contains(lexer.lexeme)){
					token=new RowIdentifier("identifier",lexer.lexeme,"");
					lexer.symTable.add(token);
					}
				else
					{
					token=lexer.symTable.getRow(lexer.lexeme);
				}
				
		}
		
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