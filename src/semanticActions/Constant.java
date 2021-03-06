package semanticActions;

import symboltable.Row;
import symboltable.RowConst;
import automaton.Lexer;

public class Constant extends SimpleAction{

	public Constant(Lexer l) {
		super(l);
	}
	
	@Override
	public Object execute(char c) {
		
		Object token=null;
		try{
			double x = Double.parseDouble(lexer.lexeme);
			if ( (x-1) > 2147483647){
				String mensaje="Constante : " +lexer.lexeme + " fuera de rango "; 
				Lexer.showError(mensaje);
			}
			else
				{
				if ((x >=0) && (x< 65536)){
					token = new RowConst("const",lexer.lexeme,"uint");
					lexer.symTable.add((Row)token);
					}
				}
			if ((x>= 65536) && ((x-1) <= 2147483647)){
				token = new RowConst("const",lexer.lexeme,"long");
				lexer.symTable.add((Row)token);
				}
			}
			catch(NumberFormatException e){
				e.printStackTrace();
			}
		return token;
	}

}
