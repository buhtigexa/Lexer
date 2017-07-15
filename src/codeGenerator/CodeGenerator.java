package codeGenerator;

import java.util.Vector;

import symboltable.Row;
import symboltable.SymbolTable;

public class CodeGenerator {

	
        fstream fileAsm;
        int identation;
        ThirdGenerator tGenerator;
        SymbolTable  symbolTable;
        RegisterBank myRegisters;
        int countAux;
        String vaAux;                 
         int noConst;
        String aux;
        int noString;
        String strConst;
        String strNum;
        
        
        
        GeneradorCodigo(String fileName, ThirdGenerator tg, SymbolTable st) {

            noConst = 0;
            aux="";
            noString = 0;
            strConst="";
            strNum="";

            fileAsm.open(fileName.c_str(), ios::out);
            this.tGenerator=tg;
			this.symbolTable = st;
			identation = 0;
			this.myRegisters= new RegisterBank();
			
			Register bx("bx");
			Register cx("cx");
			Register dx("dx");
			Register ax("ax");
			
			
			this.myRegisters.addRegister(bx);
			this.myRegisters.addRegister(cx);
			
			this.myRegisters.addRegister(dx);
			this.myRegisters.addRegister(ax);
			this.countAux=0;
			
			
			}
//------------------------------------------------------------

		public String getAuxVar(boolean  isExtended){
		
		countAux++;
		String temp;
		String count=Integer.toString(this.countAux);
		temp = "aux_" + count;
		if ( isExtended )
			writeArchivo( temp +  "   dd     0");
		else
			writeArchivo( temp  + "   dw     0");
		return temp;
		}

		public void writeArchivo(String txt) {
			String tabs = "";
			for ( int i = 0; i < identation; i++)
			    tabs += "    ";
			fileAsm << tabs + txt << endl;
		}
//------------------------------------------------------------

		public void addIdentacion() {
			identation++;
		}
//------------------------------------------------------------

		public void quiteIdentacion() {
		if (identation > 0)
		    identation--;
		}
//------------------------------------------------------------

		boolean isConmutativo(String op){
			String opMul="*";
			String opAdd="+";
			if ( ( op.compareTo(opAdd)==0 ) || ( op.compareTo(opMul)==0 ) )
			    return true;
			return false;

		}
//------------------------------------------------------------
public void generarDataSeg(){
		writeArchivo(".model small");
		writeArchivo(".radix 10");
		writeArchivo(".stack 200h");
		writeArchivo(".data");
		writeArchivo(";         aquí viene el segmento de identificadores variables y constantes.");
		downloadSymbolTable();


}
//------------------------------------------------------------

		public void downloadSymbolTable(){
			String token,type,lexeme,strAddr;
			int n =symbolTable.size();
			Row row=null;
			addIdentacion();
		
			for (  int i = 0; i < n ; i++ ){
			   row = symbolTable.getFila(i);
			    token=row.getToken();
			    type =row.getType();
			    lexeme=row.getLexeme();
			    if ( token.compareTo("Identifier")==0 )
			            if ( type.compareTo("long")==0 )
			                    writeArchivo(lexeme + "    dd      "  +  "   0   " +    ";      identificador  long");
			            else
			                    writeArchivo(lexeme + "    dw      "  +  "   0   " +    ";      identificador  uint");
			    if ( token.compareTo("String")==0 ){
			            //strAddr=IntToStr(i).c_str();
			            strAddr=Integer.toString(i);
			    		strNum="String"+ strAddr;
			            writeArchivo(strNum    +   "       db      "       +   "'" + lexeme + "',10,13,'$'");
			          }
			   }
			writeArchivo("\n;          Chequeos en tiempo de ejecución:\n");
			
			writeArchivo("runtimeExceptionDivByZero            db      '[Error]: No se puede dividir por 0.',10,13,'$'");
			writeArchivo("runtimeExceptionSubOp           db      '[Error]: Sustracción  fuera de rango.',10,13,'$'");
		}
		
//------------------------------------------------------------
		public void generarCodigo(){
			generarDataSeg();
			generarCodeSeg();
		}
/***********************************************************************************/
public void generarIDiv(Third third){

		boolean isExtended=false;
		Register regDndL("ax"), regDndH("dx"),regDsr("bx");
		String opCode = "div";
		if ( third != null){
		    if ( (third.getType()).compareTo("long")==0 ){
		            isExtended=true;
		            opCode="idiv";
		           }
		     Operand  opL = third.getLeft();
		     Operand  opR = third.getRight();
		     regDndL.setExtended(isExtended);
		     regDndH.setExtended(isExtended);
		     regDsr.setExtended(isExtended);
		     writeArchivo(" xor  " +  regDndH.getName() + ",  " + regDndH.getName() );
		     if ( (!opL.isRegistro()) && (!opR.isRegistro()) ){
		            writeArchivo( " mov  " + regDndL.getName() + ",  "  +  opL.getValue());
		            writeArchivo( " mov  " + regDsr.getName() + ",  "  +  opR.getValue());
		            runtimeTestDivByZero(regDsr);
		            writeArchivo(opCode + "    "    +  regDsr.getName());
		     }
		     if ( (!opL.isRegistro()) && (opR.isRegistro()) ){
		            writeArchivo(" pop  "  + regDsr.getName()  );
		            runtimeTestDivByZero(regDsr);
		            writeArchivo(" mov  "  + regDndL.getName() +  ",   "  + opL.getValue());
		            writeArchivo(opCode + "    "    +  regDsr.getName());
		     }
		     if ( (opL.isRegistro()) && (!opR.isRegistro()) ){
		           writeArchivo(" mov  "  + regDsr.getName() +  ",   "  + opR.getValue());
		           runtimeTestDivByZero(regDsr);
		           writeArchivo(" pop  "  + regDndL.getName()  );
		           writeArchivo(opCode + "    "    +  regDsr.getName());
		     }
		     if ( (opL.isRegistro()) && (opR.isRegistro()) ){
		           writeArchivo(" pop  "  + regDsr.getName()  );
		           runtimeTestDivByZero(regDsr);
		           writeArchivo(" pop  "  + regDndL.getName()  );
		           writeArchivo(opCode + "    "    +  regDsr.getName());
		     }
		     writeArchivo("push "   + regDndL.getName());
		}
}
//------------------------------------------------------------

void generarIMul(Third third){

		boolean  isExtended=false;
		String opCode="mul";
		Register regMulH("dx"),regMulL("ax"),regMdr("cx");
		if ( third != null){
		if ( third.getType().compareTo("long")==0){
		            isExtended=true;
		            opCode="imul";
		            }
		    Operand  opL=third.getLeft();
		    Operand  opR=third.getRight();
		    regMulH.setExtended(isExtended);
		    regMulL.setExtended(isExtended);
		    regMdr.setExtended(isExtended);
		    if ((!opL.isRegistro()) && (!opR.isRegistro() ) ) {          //  (/,VARIABLE,VARIABLE)
		            writeArchivo("mov  " + regMulL.getName() + ", "  + opL.getValue() );
		            writeArchivo("mov  " + regMdr.getName() + ",  " + opR.getValue() );
		            writeArchivo(opCode + "    "    +  regMdr.getName() );
		    }
		    if ( (!opL.isRegistro()) && ( opR.isRegistro()) ){
		            writeArchivo(" pop  "  + regMdr.getName()  );
		            writeArchivo(" mov  "  + regMulL.getName() +  ",   "  + opL.getValue());
		            writeArchivo(opCode + "    "    +  regMdr.getName());
		     }
		    if ( (opL.isRegistro()) && ( !opR.isRegistro()) ){
		            writeArchivo(" pop  "  + regMulL.getName() );
		            writeArchivo(" mov  "  + regMdr.getName() +  ",   "  + opR.getValue());
		            writeArchivo(opCode + "    "    +  regMdr.getName() );
		     }
		    if ( (opL.isRegistro()) && ( opR.isRegistro()) ){
		            writeArchivo(" pop  "  + regMdr.getName()  );
		            writeArchivo(" pop  "  + regMulL.getName()  );
		            writeArchivo(opCode + "    "    +  regMdr.getName() );
		     }
		   writeArchivo("push "   + regMulL.getName());
		}
}
//-----------------------------------------------------
void tolongRegister(Third third){

		Register regAux("cx"),regSrc("bx");
		if ( third != null){
		    Operand  opL = third.getRight();
		    if (  opL.isRegistro() )
		           {writeArchivo(";                                         tolong register ");
		                    writeArchivo(" pop  " + regSrc.getName()  + ";            sacando el registro a extender");
		                       if  (  opL.getType().compareTo("uint")==0  ) {
		                            writeArchivo(" ;                        el registro es uint.");
		                            regAux.setExtended();
		                            writeArchivo(" xor    " + regAux.getName()   + ",   " + regAux.getName());
		                            regAux.setExtended(false);
		                            writeArchivo(" mov    "   +  regAux.getName()  +  ", " +  regSrc.getName());
		                            regSrc.setExtended(); regAux.setExtended();
		                            writeArchivo("mov " + regSrc.getName() + ",  " + regAux.getName());
		                    }
		           writeArchivo(" push " + regSrc.getName() );
		        }
		}
}

//-----------------------------------------------------
void tolongMemory(Third third){

	Register regAux("bx");
	regAux.setExtended();
	Third  refL;
	String regTempL;
	if ( third != null){
	    Operand  opL = third.getRight();
	    if (  !opL.isRegistro() ) {
	            writeArchivo(";           tolong variable ");
	              if  (  opL.getType().compareTo("uint")==0  ) {
	                            writeArchivo(" ;                        la varaible es uint.");
	                            writeArchivo(" xor " + regAux.getName()   + ",  " + regAux.getName() );
	                            regAux.setExtended(false);
	                           }
	           writeArchivo(" mov " + regAux.getName()  +  ", " + opL.getValue() );
	           regAux.setExtended();
	           writeArchivo(" push " + regAux.getName() );
	          }
	   }
}
//-----------------------------------------------------
void generarTolong(Third third){
		tolongRegister(third);
		tolongMemory(third);
}
//-----------------------------------------------------
void generarIAdd(Third third){

	boolean  isExtended=false;
	Register regSrc("bx"),regDest("cx");
	if ( third != null){
	    if ( (third.getType()).compareTo("long")==0)
	            isExtended=true;
	    regSrc.setExtended( isExtended);
	    regDest.setExtended(isExtended);
	    writeArchivo(" ;                                                        suma.");
	    Operand  opL = third.getLeft();
	    Operand  opR = third.getRight();
	    if (( !opL.isRegistro()) && (!opR.isRegistro() ) ) {
	              writeArchivo(" mov " + regDest.getName() +  ", " + opL.getValue() );
	              writeArchivo(" add "+  regDest.getName() +  ", " + opR.getValue() );
	    }
	    if ( ( !opL.isRegistro() ) && ( opR.isRegistro() ) ) {// ( + ,VARIABLE, REGISTRO )
	              writeArchivo( " pop  " +  regDest.getName() );
	              writeArchivo( " add   " + regDest.getName() + ", " + opL.getValue() );
	    }
	    if (( opL.isRegistro()) && (!opR.isRegistro() ) ) {
	              writeArchivo( " pop  " +  regDest.getName() );
	              writeArchivo( " add   " + regDest.getName() + ", " + opR.getValue() );
	    }
	    if (( opL.isRegistro()) && (opR.isRegistro() ) ) {// ( + ,REGISTRO,REGISTRO )
	               writeArchivo(" pop " + regSrc.getName());
	               writeArchivo(" pop " + regDest.getName());
	               writeArchivo("add  " + regDest.getName() + ", " + regSrc.getName() );
	   }
	
	writeArchivo("push " + regDest.getName() );
	writeArchivo( "                            ; ( + , " + regDest.getName()  + ",  " +  regSrc.getName()  +")" );
	
	  }
}
//------------------------------------------------------------
public void generarISub(Third third){

	boolean  isExtended=false;
	Register regSrc("bx"),regDest("cx");
	if ( third != null){
	    if ( (third.getType()).compareTo("long")==0)
	            isExtended=true;
	    regSrc.setExtended( isExtended);
	    regDest.setExtended(isExtended);
	    writeArchivo(" ;                                                        Resta.");
	    Operand  opL = third.getLeft();
	    Operand  opR = third.getRight();
	    if (( !opL.isRegistro()) && (!opR.isRegistro() ) ) {
	              writeArchivo(" mov " + regDest.getName() +  ", " + opL.getValue() );
	              writeArchivo(" sub "+  regDest.getName() +  ", " + opR.getValue() );
	    }
	    if (( !opL.isRegistro()) && ( opR.isRegistro() ) ) {
	              writeArchivo(" pop  " + regSrc.getName());
	              writeArchivo(" mov  " + regDest.getName() + ",  " + opL.getValue());
	              writeArchivo(" sub "+   regDest.getName() +  ", " + regSrc.getName() );
	    }
	    if ((  opL.isRegistro()) && ( !opR.isRegistro() ) ) {
	              writeArchivo(" pop   "   +  regDest.getName());
	              writeArchivo(" sub "+  regDest.getName() +  ", " + opR.getValue() );
	    }
	    if (( opL.isRegistro()) && ( opR.isRegistro() ) ) {
	              writeArchivo(" pop   "    +   regSrc.getName());
	              writeArchivo(" pop   "    +   regDest.getName());
	              writeArchivo(" sub "+  regDest.getName() +  ", " + regSrc.getName() );
	    }
	    runtimeTestSub(Third,regDest.getName());
	    writeArchivo( " push " + regDest.getName());
	     }
}
//------------------------------------------------------------
public void generarIAssign(Third third){

	boolean isExtended=false;
	Register regAux("dx"),regSrc("ax");
	if ( third != null){
	    if ( (third.getType()).compareTo("long")==0)
	            isExtended=true;
	    regSrc.setExtended(isExtended);
	    regAux.setExtended(isExtended);
	    Operand  opL = third.getLeft();
	    Operand  opR = third.getRight();
	    if (( !opL.isRegistro()) && (!opR.isRegistro() ) ) {//  (:=,VARIABLE,VARIABLE)
	
	            writeArchivo(" mov  " + regAux.getName() + ",  " + opR.getValue());
	            writeArchivo(" mov  " + opL.getValue()  + ",  "   + regAux.getName());
	          }
	    if (( !opL.isRegistro()) && (  opR.isRegistro() ) ) {
	                    writeArchivo(" pop   " + regAux.getName() );
	                    writeArchivo(" mov  " +  opL.getValue() + ",  " + regAux.getName()   );
	         }
	}


}
//------------------------------------------------------------
public void generarPrint(Third third){

		String strAddr,strAux;
		int i=0;
		if (third != null){
	            i=this.symbolTable.indexSymbol((third.getRightOp()).getValue());
	            strAux = Integer.toString(i);
	            strNum="String"+ strAux;
	            writeArchivo("mov ah, 09");
	            writeArchivo("lea dx, "+ strNum);
	            writeArchivo("int 21h");
	
	}

}
//------------------------------------------------------------
public void generarCmp(Third third){

	Register regL("bx"),regR("dx");
	
	myRegisters.setBusy(regL);
	myRegisters.setBusy(regR);
	boolean  isExtended=false;
	
	if ( third != null){
	    Operand opL = third.getLeft();
	    Operand opR = third.getRight();
	
	    if ( third.getType().compareTo("long")==0 )
	              isExtended=true;
	    regL.setExtended(isExtended);
	    regR.setExtended(isExtended);
	    if ( ( !opL.isRegistro() )  && ( !opR.isRegistro() ) ) { //  ( cmp, VARIABLE, VARIABLE )
	            writeArchivo (" mov " +   regL.getName() +  ",  "  + opL.getValue() );
	            writeArchivo (" cmp "  +  regL.getName() + ",  "  + opR.getValue() );
	      }
	    if ( ( !opL.isRegistro() )  && (opR.isRegistro()  ) ){ //   ( cmp, VARIABLE, REGISTRO  )
	            writeArchivo (" mov " +  regL.getName() +  ",  "  + opL.getValue() );
	            writeArchivo (" pop " +  regR.getName() );
	            writeArchivo (" cmp "  + regL.getName() + ",  " +  regR.getName() );
	           }
	    if ( ( opL.isRegistro() )  && ( !opR.isRegistro() ) ){ //  ( cmp, REGISTRO, VARIABLE  )
	            writeArchivo(" pop "  + regL.getName());
	            writeArchivo(" cmp "  + regL.getName() + ",   " + opR.getValue() ) ;
	     }
	    if ( ( opL.isRegistro() )  && ( opR.isRegistro()  ) ){ //   ( cmp, REGISTRO, REGISTRO  )
	            writeArchivo(" pop " + regR.getName());
	            writeArchivo(" pop " + regL.getName());
	            writeArchivo("cmp  " + regL.getName() + ", " + regL.getName());
	    }
 }


//------------------------------------------------------------
void generarFBranch(Third third){
		
		String cond, labelJmp,jmpType;
		
		if ( ( third != null)  ) {
		
		    cond = third.getOpPrevious();
		    labelJmp= third.getLabelSrc();
		    jmpType = this.getJmp(third.getType(),cond);
		    writeArchivo(jmpType + "    " + labelJmp);
		   }
}
//------------------------------------------------------------
public void generarIBranch(Third third){
		String labelJmp,jmpType;
		if ( ( third !=null ) ) {
		    labelJmp = third.getLabelSrc();
		    jmpType = this.getJmp("sinTipo","incondicional");
		    writeArchivo(jmpType +  "    " + labelJmp);
		 }
}
//------------------------------------------------------------
public String getJmp(String type, String cond){
		
			String tempJ="jmp";
			if ( type.compareTo("uint")==0){
			            if ( cond.compareTo("<>")==0)
			                    tempJ="je";                     // =
			            if ( cond.compareTo("<=")==0)
			                    tempJ="ja";                     // >
			            if ( cond.compareTo(">=")==0)
			                    tempJ="jb";                     // <
			            if ( cond.compareTo("=")==0)
			                    tempJ="jne";                    // <>
			            if ( cond.compareTo(">")==0)
			                    tempJ="jbe";                    // <=
			            if ( cond.compareTo("<")==0)
			                    tempJ="jae";                    // >=
			}
			if ( type.compareTo("long")==0){
			                   if ( cond.compareTo("<>")==0)
			                    tempJ="je";                     // =
			            if ( cond.compareTo("<=")==0)
			                    tempJ="jg";                     // >
			            if ( cond.compareTo(">=")==0)
			                    tempJ="jl";                     // <
			            if ( cond.compareTo("=")==0)
			                    tempJ="jne";                    // <>
			            if ( cond.compareTo(">")==0)
			                    tempJ="jle";                    // <=
			            if ( cond.compareTo("<")==0)
			                    tempJ="jge";                    // >=
			
			}
		return tempJ;
	}

//------------------------------------------------------------
void runtimeTestDivByZero(Register registerDsr){
	writeArchivo("cmp  " + registerDsr.getName() + ", 0h");
	writeArchivo("jz errDivByZero");
}
//------------------------------------------------------------
void runtimeTestSub(Third third,String registerOp){
	

	if ( ( third != null) && ( third.getType().compareTo("uint")==0 ) ){
	    writeArchivo(" cmp  " + registerOp   +  ",0h ");
	    writeArchivo("jl errSub");
	}

}
//------------------------------------------------------------
/*********************************************************************************************************************************/
public void generarCodeSeg(){

	writeArchivo(".code");
	writeArchivo(".386");
	writeArchivo(".387");
	Vector<Third> myThirds = tGenerator.getTercetos();
	writeArchivo("start:");
	addIdentacion();
	addIdentacion();
	writeArchivo("                  mov ax,@data ");
	writeArchivo("                  mov ds,ax   ");
	writeArchivo("                  jmp start_code");
	quiteIdentacion();
	writeArchivo("errDivByZero:");
	addIdentacion();
	writeArchivo("                  mov ah,09 ");
	writeArchivo("                  mov dx, offset runtimeExceptionDivByZero");
	writeArchivo("                  int 21h ");
	writeArchivo("                  jmp exit_code ");
	quiteIdentacion();
	writeArchivo("errSub:");
	addIdentacion();
	writeArchivo("                  mov ah,09 ");
	writeArchivo("                  mov dx, offset runtimeExceptionSubOp");
	writeArchivo("                  int 21h ");
	writeArchivo("                  jmp exit_code ");
	writeArchivo("start_code:");
	this.tGenerator.showTercetos();
	Third  temp;
	String labelDestiny;
	if ( myThirds != null ){
	    tGenerator.labeler("exit_code");
	     int nThirds=myThirds.size();
	    for (  int index=0;index < nThirds; index++)  {
	
	          writeArchivo("\n");
	
	          temp =  myThirds.get(index);
	          this.showThird(temp);
	          if ( temp!=null) { labelDestiny = temp.getLabelDst(); if ( !labelDestiny.isEmpty() ) writeArchivo(labelDestiny + ":");  }
	          if ( ( temp.getOperator()).compareTo("+")==0 )
	               generarIAdd(temp);
	          if ( (temp.getOperator()).compareTo(":=")==0 )
	               generarIAssign(temp);
	          if ( (temp.getOperator()).compareTo("-")==0 )
	               generarISub(temp);
	          if ( (temp.getOperator()).compareTo("*")==0 )
	               generarIMul(temp);
	          if ( (temp.getOperator()).compareTo("print")==0 )
	               generarPrint(temp);
	          if ( (temp.getOperator()).compareTo("/")==0 )
	               generarIDiv(temp);
	          if ( (temp.getOperator()).compareTo("tolong")==0 )
	               generarTolong(temp);
	          if (( ( temp.getOperator().compareTo(">")==0  ) || ( temp.getOperator().compareTo("<")==0  ) )||
	            ( ( temp.getOperator().compareTo(">=")==0 ) || ( temp.getOperator().compareTo("<=")==0 ) )||
	            ( ( temp.getOperator().compareTo("=")==0  ) || ( temp.getOperator().compareTo("<>")==0 )  )
	            )
	              generarCmp(temp);
	
	          if ( (temp.getOperator()).compareTo("BF")==0 ){
	                    generarFBranch(temp);
	          }
	          if ( (temp.getOperator()).compareTo("BI")==0 )
	                    generarIBranch(temp);
	         }
			}
			// fin de instrucciones ejecutables.
			writeArchivo("\n");
			quiteIdentacion();
			writeArchivo("exit_code:");
			addIdentacion();
			writeArchivo("                  mov ah,04ch");
			writeArchivo("                  mov al,00h");
			writeArchivo("                  int 21h");
			quiteIdentacion();
			writeArchivo("end start  ");
			writeArchivo("\n  ");
	}

//------------------------------------------------------------
public void showThird( Third  temp){
		String left,right;
        if ( temp.getLeftOp().isSalto() )
               left = "[" + (temp.getLeftOp()).getValue() + "]";
          else  left =(temp).getLeftOp().getValue();
         if ( temp.getRightOp().isSalto() )
              right = "[" + (temp).getRightOp().getValue() + "]";
         else  right =(temp).getRightOp().getValue();
         	String i= Integer.toString((temp.getId()));
         	//TForm1::writer5( i  +  "  ( " + (temp).getOperator() + " ,  " +  left + " ,  " + right + " ) " );// + "- Src:   " + temp.getLabelSrc()  + " -Dest:   "  + temp.getLabelDst() + "-OpPrev:   "  + temp.getOpprev());
         	System.out.println(i  +  "  ( " + (temp).getOperator() + " ,  " +  left + " ,  " + right + " ) ");
}
//------------------------------------------------------------

}
