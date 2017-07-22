

import parser.Parser;
import symboltable.NameDecorator;
import symboltable.SymbolTable;
import utils.Console;
import utils.ErrorConsole;
import utils.MyFStream;
import utils.WarningConsole;
import automaton.Lexer;
import codeGenerator.GeneradorASM;
import codeGenerator.ThirdGenerator;

public class Test {

	/**
	 * @param args
	 */
	
	
	
	public void setUp(String file){
		

		MyFStream fstream = new MyFStream(file+".asm");
		
		String symbolFile="/home/marcelo/workspace/test_files/symbolTable.txt";
		String errorFile="/home/marcelo/workspace/test_files/errors.txt";
		String warningFile="/home/marcelo/workspace/test_files/warnings.txt";
		
		NameDecorator nameDecorator=new NameDecorator();
		SymbolTable symbolTable = new SymbolTable(nameDecorator,symbolFile);
		Lexer lexer = new Lexer(file,symbolTable,errorFile,warningFile,true,false,false);
		ThirdGenerator thirdGenerator=new ThirdGenerator(lexer);

				
		Parser parser = new Parser();
		parser.load(lexer, symbolTable,thirdGenerator,nameDecorator);
		parser.run();
		GeneradorASM generadorASM=new GeneradorASM( thirdGenerator, symbolTable,fstream);

		generadorASM.generarCodigo();

		System.out.println("-----------Programa aceptado : -------- "  + file +  " Errors : " + lexer.errors);
		
		System.out.println(thirdGenerator);
		System.out.println(symbolTable);
		
		Lexer.saveMessages();
		symbolTable.saveTable();
		
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Test t = new Test();
		
		String programas[]={
				
				"/home/marcelo/workspace/test_files/print.txt",
				"/home/marcelo/workspace/test_files/loop.txt",
				"/home/marcelo/workspace/test_files/tp2-ambitos-32-ok.txt",
				"/home/marcelo/workspace/test_files/entrega1.txt",
				"/home/marcelo/workspace/test_files/errUnsignedSub.txt",
				"/home/marcelo/workspace/test_files/errdivByZero.txt"
				
		};
		
		//for (int i=0; i < 6; i++){
			t.setUp(programas[2]);
		//}
		
		
		
		
	}
}

