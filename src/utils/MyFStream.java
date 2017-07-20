package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MyFStream {

	int identation;
	String fileAsm;
	File file ;
	PrintWriter printWriter;
	
	public MyFStream(String path){
		
		fileAsm=new String();
		identation=0;
		file = new File(path);
		try {
			printWriter=new PrintWriter(file);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		
		
	}
	
	public void endWrite(){
		
		printWriter.close();
		
	}
	
	public void writeArchivo(String txt) {
		
		String tabs = "";
		fileAsm="";
		for ( int i = 0; i < identation; i++)
		    tabs += "    ";
		fileAsm+=tabs + txt;
		fileAsm+="\n";
		printWriter.write(fileAsm);
		System.out.println(txt);
		
		
	}
	
	public void addIdentacion() {
		identation++;
	}
	
	public void quiteIdentacion() {
		
		if (identation > 0)
			identation--;
	}
	
	/*
	public void save(){
	
		
		printWriter = null;
		try{
	        printWriter = new PrintWriter(file);
	        printWriter.println("hello");
	        

	    }
	    catch (FileNotFoundException e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        if ( printWriter != null ) 
	        {
	            printWriter.close();
	        }
	    }
		
	
	}
	*/
	
	public String toString(){
		
		return fileAsm;
	}


}



