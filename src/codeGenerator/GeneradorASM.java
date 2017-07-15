package codeGenerator;

import symboltable.SymbolTable;

public class GeneradorASM extends CodeGenerator {

	public GeneradorASM(String fileName, ThirdGenerator tg, SymbolTable st) {
		super(fileName, tg, st);
		// TODO Auto-generated constructor stub
	}
	
	boolean isConmutativo(String op){

        String opMul = "*";
        String opAdd = "+";
        if ( ( op.compareTo(opMul)==0 )|| ( op.compareTo(opAdd)==0 ) )
                return true;
        return false;

}

}
