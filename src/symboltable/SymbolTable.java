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
			str+= "\n " + row;
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
	      	
}


