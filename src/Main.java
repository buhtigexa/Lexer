

import generadorAssembler.GeneradorAssembler;
import generadorCodigo.GeneradorTercetos;
import parser.Parser;
import symboltable.NameDecorator;
import symboltable.SymbolTable;
import utils.MyFStream;
import automaton.Lexer;

public class Main {

	/**
	 * @param args
	 */
	
	
	
	public void setUp(String fileToParse,String generatedPath,String output){
		
		String fileName="";
		MyFStream fstream = new MyFStream(generatedPath+"/"+fileName+".asm");
		
		String symbolFile=generatedPath+"/symbolTable.txt";
		String errorFile =generatedPath+"/errors.txt";
		String warningFile=generatedPath+"/warnings.txt";
		String interCode=generatedPath+"/tercetos.txt";
		
		NameDecorator nameDecorator=new NameDecorator();
		SymbolTable symbolTable = new SymbolTable(nameDecorator,symbolFile);
		Lexer lexer = new Lexer(fileToParse,symbolTable,errorFile,warningFile,true,true,true);
		GeneradorTercetos generadorTercetos=new GeneradorTercetos(lexer,interCode);

				
		Parser parser = new Parser();
		parser.load(lexer, symbolTable,generadorTercetos,nameDecorator);
		parser.run();
		
		GeneradorAssembler generadorASM=new GeneradorAssembler(fstream, generadorTercetos, symbolTable);

		generadorASM.generarCodigo();

		System.out.println("-----------Programa aceptado : -------- "  + fileToParse +  " Errors : " + lexer.errors);
		
		
		
		Lexer.saveMessages();
		symbolTable.saveTable();
		System.out.println(generadorTercetos);
		
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Main t = new Main();
		
		String programas[]={
				
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/plus_plus.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/loop.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/tp2-ambitos-32-ok.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/entrega1.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/errUnsignedSub.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/errdivByZero.txt"
				
		};
	
		t.setUp("/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/","errdivByZero.txt","/home/marcelo/workspace/Lexer/generated/");
		
		
		
		
	}
}

