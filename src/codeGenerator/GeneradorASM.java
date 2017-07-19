package codeGenerator;

import symboltable.SymbolTable;
import utils.MyFStream;

public class GeneradorASM extends CodeGenerator {

	public GeneradorASM( ThirdGenerator tg, SymbolTable st,MyFStream fstream) {
		super( tg, st,fstream);
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
