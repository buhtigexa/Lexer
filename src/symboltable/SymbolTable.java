package symboltable;

import java.util.Vector;


public class SymbolTable {

	
	public Vector<Row> rows;
	public NameDecorator decorator;
	
	public NameDecorator getDecorator() {
		return decorator;
	}

	public SymbolTable(NameDecorator decorator){
		
		rows=new Vector<Row>();
		this.decorator=decorator;
		
	}
	
	public int size(){
		
		return rows.size();
	}
	
	public void add(Row row){
		
		this.rows.add(row);
		//System.out.println(this);
	}
	
	
	public String toString(){
	
		System.out.println("------------------- Symbol Table Starts ------------------------------------");
		String str=new String();
		
		for (int i=0; i< rows.size();i++){
			System.out.println(rows.get(i));
		}
		
		System.out.println("------------------- Symbol Table Ends ------------------------------------");
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
}


