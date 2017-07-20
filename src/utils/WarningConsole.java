package utils;

import automaton.Lexer;

public class WarningConsole extends Console{

	public WarningConsole(String path, String consoleName, Lexer l) {
		super(path, consoleName,l);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show(String m){
		
		Lexer.warnings++;
		super.show(m);
		
	}
}
