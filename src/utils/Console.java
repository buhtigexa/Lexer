package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import automaton.Lexer;

public  class Console {

	public File file;
	public PrintWriter printWriter;
	public String text;
	public int identation;
	public String name;
	public Lexer lexer;
	
	public Console(String path,String consoleName, Lexer lexer){
		
		
		this.name=consoleName;
		text = new String();
		identation=0;
		this.lexer=lexer;
		file = new File(path);
		try {
			printWriter=new PrintWriter(file);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}

	public void writeToFile(){
		
		printWriter.close();
	}
	
	
	public void show(String m){
		
		text="\n " + name   + "  (" + lexer.line + ","  + lexer.column + ") :  " + m;
		printWriter.write(text);
	}
}


