package codeGenerator;

import symboltable.Row;
import symboltable.SymbolTable;

public class Operand {

	String value;
    Row tsValue;
    boolean salto;
    int posicion;
    String type;
    SymbolTable symbolTable;
    boolean reg;



 public Operand(Row tsV,SymbolTable st){
 
    if ( tsV!=null){
        value=tsV.getLexeme();
        tsValue=tsV;
        }
    salto=false;
    posicion=0;
    symbolTable=st;
    reg=false;
}

public Operand(String v,Row tsV,SymbolTable st){

	if ( tsV!=null){
        value=tsV.getLexeme();
        tsValue=tsV;
       }
      salto=false;
      posicion=0;
      symbolTable=st;
      reg=false;
}

public Operand(String v,int i){
     
	 value=Integer.toString(i);
	 tsValue=null;
     posicion=i;
     salto=true;
     symbolTable=null;
     reg=true;
}
public Operand(int i){
     
	value=Integer.toString(i);
    tsValue=null;
    posicion=i;
    salto=true;
    symbolTable=null;
    reg=true;

}

public Operand(){

	  value="";
      tsValue=null;
      salto=false;
      posicion=0;
      symbolTable=null;
      reg=false;
}

public Operand(Operand op){

	if ( op!=null) {
           value=op.getValue();
           tsValue=op.getTsValue();
           salto=op.isSalto();
           posicion=op.getPosicion();
           symbolTable=op.getSymbolTable();
           reg=op.isRegistro();
      }
}

public void showOperando(){

}


public int getPosicion(){
      return posicion;
}

public boolean isSalto(){
      return salto;

}

public Row getTsValue(){
      return tsValue;
}

public String getValue(){
      return value;

}
boolean isRegistro(){
      return reg;
}


public SymbolTable getSymbolTable() {

      return symbolTable;
}

public String getType(){

	String tempType="";
    if (!(isSalto()) && ( (tsValue) != null )   ){
    	Row f =  symbolTable.getRow(value);
        if ( f != null ){
            tempType = f.getType();
         }
      }
      else if ( isSalto() )
    	tempType=Integer.toString(getPosicion());
    return tempType;
 }
}

