package utils;

import automaton.Lexer;

public class ErrorConsole extends Console{

	public ErrorConsole(String path,String consoleName, Lexer l) {
		super(path,consoleName,l);
		
		
	}

	@Override
	public void show(String m) {
		
		Lexer.errors++;
		super.show(m);
		

	}

}
