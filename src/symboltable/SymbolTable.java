package symboltable;

import java.util.Vector;


public class SymbolTable {

	
	public Vector<Row> rows;
	
	
	public SymbolTable(){
		
		rows=new Vector<Row>();
		
	}
	
	public void add(Row row){
		
		this.rows.add(row);
	}
	
	
	public String toString(){
		
		String str=new String();
				
		for (Row row:rows){
			str+= "\n " + rows;
		}
		return str;
		
	}
}
