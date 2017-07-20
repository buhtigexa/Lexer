

import parser.Parser;
import symboltable.NameDecorator;
import symboltable.SymbolTable;
import utils.MyFStream;
import automaton.Lexer;
import codeGenerator.GeneradorASM;
import codeGenerator.ThirdGenerator;

public class Test {

	/**
	 * @param args
	 */
	
	
	
	public void setUp(String file){
		

		MyFStream fstream = new MyFStream(file+".asm");
		
		NameDecorator nameDecorator=new NameDecorator();
		SymbolTable symbolTable = new SymbolTable(nameDecorator);
		Lexer lexer = new Lexer(file,symbolTable);
		ThirdGenerator thirdGenerator=new ThirdGenerator(lexer);
		
				
		Parser parser = new Parser();
		parser.load(lexer, symbolTable,thirdGenerator,nameDecorator);
		parser.run();
		GeneradorASM generadorASM=new GeneradorASM( thirdGenerator, symbolTable,fstream);

		
		
		generadorASM.generarCodigo();

		System.out.println("-----------Programa aceptado : -------- "  + file +  " Errors : " + lexer.errors);
		
		System.out.println(thirdGenerator);
		System.out.println(symbolTable);
		
	
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
		
		
		t.setUp(programas[0]);
		
		
		
		
		
	}
}

