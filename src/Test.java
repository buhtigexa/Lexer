

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
		
		System.out.println("-----------Programa aceptado : -------- "  + s);
		
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Test t = new Test();
		
		
		//t.setUp("/home/marcelo/workspace/test_files/errUnsignedSub.txt");
		
		t.setUp("/home/marcelo/workspace/test_files/errdivByZero.txt");
		
		/* Con esto funciona.:
		 * 
		 * 
		 * t.setUp("/home/marcelo/workspace/test_files/tp2-ambitos-32-ok.txt");
		 * 
		 * programa1:"/home/marcelo/workspace/test_files/entrega1.txt";
		 * 
		 * programa2:/home/marcelo/workspace/test_files/loop.txt;
		 * 
		 * programa3:/home/marcelo/workspace/test_files/errUnsignedSub.txt;
		 * 
		 * 
		 * 
		 * 
		 * public int yylex(){
  
			boolean eof = false;
			eof = lex.endOfFile();
				
			if (eof){
				return  0;//(Short)codes.get("ENDOFFILE");
			} 
			Row tok = (Row) lex.getToken();
			if(tok == null){
			  Short x = (Short) codes.get("ENDOFFILE");
			  return x;
			}
			yylval = new ParserVal(tok);
			Short s = (Short) codes.get(tok.getToken());
			System.out.println("[ PARSER - TOKEN  ] " + tok);
			return s.intValue();
		} 
		*/
		
		
		
		
		
		t.setUp("/home/marcelo/workspace/test_files/errdivByZero.txt");
		
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

