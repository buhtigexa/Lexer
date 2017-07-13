

import parser.Parser;
import symboltable.SymbolTable;
import automaton.Lexer;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		
		SymbolTable symbolTable = new SymbolTable();
		Lexer lexer = new Lexer("/home/marcelo/workspace/test_files/entrega1.txt",symbolTable);
		
		
		Parser parser = new Parser();
		parser.load(lexer, symbolTable);
		parser.run();
		
		
		/*
		Object token=null;
				
		for (int i=0; i <280;i++){
			token=lexer.getToken();
			
			System.out.println();
			System.out.println("LEXER -- TOKEN " + token);
		}
		
		System.out.println("--------  SYMBOL TABLE ---------------------");
		System.out.println(symbolTable);
		
		try
		{
			String t=(String)token;
			if (t.equals("T_EOF")){
				System.out.println(" >>>> END OF FILE <<<<");
			}
		}
		
		catch(NullPointerException e){
			
			System.out.println(" >>>> END OF FILE  <<<<");
		}
		catch(ClassCastException e){

			System.out.println(" >>>> END OF FILE EXCEPTION <<<<");
		
		}
		
		*/
		
		
	}
}

