

import generadorAssembler.GeneradorAssembler;
import generadorCodigo.GeneradorTercetos;

import java.io.File;
import java.io.FileNotFoundException;

import parser.Parser;
import symboltable.NameDecorator;
import symboltable.SymbolTable;
import utils.MyFStream;
import automaton.Lexer;

public class Main {

	/**
	 * @param args
	 */
	
	
	
	public void setUp(File fileToParse,String generatedPath,String output){
		
		String fileName= fileToParse.getName();
		MyFStream fstream = new MyFStream(generatedPath+"/"+fileName.replace(".txt", ".asm").toLowerCase());
		
		String symbolFile=output+"/symbolTable.txt";
		String errorFile =output+"/errors.txt";
		String warningFile=output+"/warnings.txt";
		String interCode=output+"/tercetos.txt";
		
		NameDecorator nameDecorator=new NameDecorator();
		SymbolTable symbolTable = new SymbolTable(nameDecorator,symbolFile);
		Lexer lexer = new Lexer(fileToParse.getAbsolutePath(),symbolTable,errorFile,warningFile,false,true,true);
		GeneradorTercetos generadorTercetos=new GeneradorTercetos(lexer,interCode);

				
		Parser parser = new Parser();
		parser.load(lexer, symbolTable,generadorTercetos,nameDecorator);
		parser.run();
		
		GeneradorAssembler generadorASM=new GeneradorAssembler(fstream, generadorTercetos, symbolTable);

		generadorASM.generarCodigo();

		System.out.println("\n Compilando ... "+  fileToParse +  " con Errors : " + lexer.errors);
		
		
		
		Lexer.saveMessages();
		symbolTable.saveTable();
		
		/*
		System.out.println(symbolTable);
		System.out.println(generadorTercetos);
		*/
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Main t = new Main();
		
		System.out.println(" ---------------------------------------------------------------- ");
		System.out.println(" ");
		System.out.println(" 	Universidad Nacional del Centro de la Provincia de Buenos Aires ");
		System.out.println(" ");
		System.out.println(" 		Cátedra : Diseño de Compiladores ");
		System.out.println(" 		Extensión del compilador : incorporación del operador '++' a identificadores tipo long ");
		System.out.println(" ");
		System.out.println(" 	Prof: Mg. Ing. Marcela Ridado ");
		System.out.println(" ");
		System.out.println(" 	Sr: Marcelo Rodríguez -- mrodriguez@alumnos.unicen.edu.ar");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" Uso : [1]path al programa [2] path al directorio del archivo assembler generado [3] directorio para guardar la salida  ");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" ---------------------------------------------------------------- ");
		
		
		String programas[]={
				
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/plus_plus.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/loop.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/tp2-ambitos-32-ok.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/entrega1.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/errUnsignedSub.txt",
				"/home/marcelo/workspace/Lexer/test_files/succeeded/extension_++/errdivByZero.txt"
				
		};
	
		if (args.length!=3){
			System.out.println(" Error - Faltan argumentos ");
			System.out.println(" Uso : [1]path al programa [2] path al directorio de archivos assembler [3] directorio para guardar la salida  ");
			//return;
		}
		
		
		
	
		String programa=args[0];
		String asmDir=args[1];
		String outputDir=args[2];
		
		
		File asm = new File(asmDir);
		
		
		if (!asm.exists()){
			boolean result=false;
			try{
     		   	asm.mkdir();
        		result = true;
    			} 
			    catch(SecurityException se){
			        //handle it
			    }        
			    if(result) {    
			        System.out.println("Directorio para archivos assembler creado.");  
			    }
			}
		
		File output=new File(outputDir);
		
		if (!output.exists()){
			boolean result=false;
			try{
     		   	output.mkdir();
        		result = true;
    			} 
			    catch(SecurityException se){
			        //handle it
			    }        
			    if(result) {    
			        System.out.println("Directorio para archivos de salida creado");  
			    }
			}
		
		
		File file = null;
		
		
		file = new File(programa);

		if (!file.exists()){
			System.out.println(" No hay programa para compilar .");
			return;
		}
		
		t.setUp(file,asmDir,outputDir);
			
		
		
		
		
		
	}
}

