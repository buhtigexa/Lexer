package symboltable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;


public class SymbolTable {

	
	public Vector<Row> rows;
	public NameDecorator decorator;
	public File fileTable;
	public PrintWriter printWriter;
	
	public NameDecorator getDecorator() {
		return decorator;
	}

	public SymbolTable(NameDecorator decorator,String path){
		
		rows=new Vector<Row>();
		this.decorator=decorator;
		fileTable=new File(path);
		try {
			printWriter=new PrintWriter(fileTable);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public int size(){
		
		return rows.size();
	}
	
	public void add(Object row){
		
		this.rows.addElement((Row)row);
		//System.out.println(this);
	}
	
	
	public String toString(){
	
		String str=new String();
		
		for (int i=0; i< rows.size();i++){
			System.out.println( rows.get(i).prettyPrint());
		}
		
		return str;
		
	}
	
	public Row getRow(int i){
		
		if (i < this.rows.size() ){
			return rows.elementAt(i);
		}
		return null;
	}
	
	public Row getRow(String lexeme){
	
		for (int i=0; i < rows.size();i++){
			if (rows.get(i).getLexeme().equals(lexeme)){
				return rows.get(i);
			}
		}
		return null;
	}
	    

	public boolean contains(String lexeme)
	{
	        if (getRow(lexeme)!= null)
	                return true;
	        return false;
	}

	
	public int indexSymbol(String str){
        for (int i=0 ; i< rows.size() ; i++)
                if ( rows.get(i).getLexeme().compareTo(str)==0)
                        return i;
        return -1;
	}
	
	public void saveTable(){
		
		String str=new String();
			for (int i=0; i< rows.size();i++){
				//str=rows.get(i).toString() + "\n";
				str=  rows.get(i).prettyPrint() + "\n";
				printWriter.write(str);
		}
			
		printWriter.close();
	
	}
	
	// a method to seek by type and lexeme
	
	public Row find(String lexeme, String type){
		
		for (Row r: rows){
			if ( ( r.getType().compareToIgnoreCase(type)==0) && (r.getLexeme().compareToIgnoreCase(lexeme)==0) ){
				return r;
			}
		}
		return null;
	}
	
}


