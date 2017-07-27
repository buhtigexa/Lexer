package semanticActions;

import automaton.Lexer;

public class IllegalCharacter extends SimpleAction {

	public IllegalCharacter(Lexer l) {
		super(l);
		
	}

	@Override
	public Object execute(char c) {
	
		String mensaje="";
		if ((short)c < 0 || ((short)c)==46){
			return null;
		}
		mensaje="[Error ] caracter ilegal " + c  + "Codigo de caracter : "  + (short)c;
		Lexer.showError(mensaje);
		return null;
	}

}
