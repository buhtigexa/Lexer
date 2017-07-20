package utils;

import automaton.Lexer;

public class ErrorConsole extends Console{

	public ErrorConsole(String path,String consoleName) {
		super(path,consoleName);
		
		
	}

	@Override
	public void show(String m) {
		
		Lexer.errors++;
		super.show(m);
		

	}

}
