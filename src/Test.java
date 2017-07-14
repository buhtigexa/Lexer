

import parser.Parser;
import parser.ParserVal;
import symboltable.Row;
import symboltable.SymbolTable;
import automaton.Lexer;

public class Test {

	/**
	 * @param args
	 */
	
	public void setUp(String s){
		
		
		SymbolTable symbolTable = new SymbolTable();
		Lexer lexer = new Lexer(s,symbolTable);
			
		Parser parser = new Parser();
		parser.load(lexer, symbolTable);
		parser.run();
		
		System.out.println("-----------Programa aceptado : -------- "  + s +  " Errors : " + lexer.errors);
		
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Test t = new Test();
		
		String programas[]={
				
				"/home/marcelo/workspace/test_files/tp2-ambitos-32-ok.txt",
				"/home/marcelo/workspace/test_files/entrega1.txt",
				"/home/marcelo/workspace/test_files/loop.txt",
				"/home/marcelo/workspace/test_files/errUnsignedSub.txt",
				"/home/marcelo/workspace/test_files/errdivByZero.txt"
				
		};
		
		for (int i=0;i< 5;i++){
			t.setUp(programas[i]);
		}
		
		t.setUp("/home/marcelo/workspace/test_files/errdivByZero.txt");
		
		
		
		
	}
}

