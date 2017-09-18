package semanticActions;

import automaton.Lexer;

public class WhiteChar extends SimpleAction{

	public WhiteChar(Lexer l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(char c) {
		// TODO Auto-generated method stub
		
		int code=(char)c;
		
		// TAB
		if (code == 9){
			lexer.column+=3;
		}
		// New Line \n 		\n tiene sentido en unix
		if (code == 10) {
			++lexer.line;
			lexer.column=0;
		}
		// Carriage return \r   \n\r tiene sentido en windows
		if (code == 13){
			lexer.column=0;
			//lexer.line++;
		}
		
		return null;
	}

}
