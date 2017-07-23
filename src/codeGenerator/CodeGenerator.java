package codeGenerator;

import java.util.Vector;

import automaton.Lexer;

import symboltable.Row;
import symboltable.SymbolTable;
import utils.MyFStream;

public class CodeGenerator {

	
        //fstream fileAsm;
    
		MyFStream fstream;
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
        
        
        
        
        public CodeGenerator(ThirdGenerator tg, SymbolTable st,MyFStream fstream) {

        	this.fstream=fstream;
        	
        	noConst = 0;
            aux="";
            noString = 0;
            strConst="";
            strNum="";

            //fileAsm.open(fileName.c_str(), ios::out);
            
            this.tGenerator=tg;
			this.symbolTable = st;
			identation = 0;
			this.myRegisters= new RegisterBank();
			
			Register bx=new Register("bx");
			Register cx=new Register("cx");
			Register dx=new Register("dx");
			Register ax=new Register("ax");
			
			
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
			
			fstream.writeArchivo( temp +  "   dd     0");
		else
			
			fstream.writeArchivo( temp  + "   dw     0");
		
			return temp;
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
		
			fstream.writeArchivo(".model small");
			fstream.writeArchivo(".radix 10");
			fstream.writeArchivo(".stack 200h");
			fstream.writeArchivo(".data");
			fstream.writeArchivo(";         aquí viene el segmento de identificadores variables y constantes.");
			downloadSymbolTable();


}
//------------------------------------------------------------

		public void downloadSymbolTable(){
			
			String token,type,lexeme,strAddr;
			int n =symbolTable.size();
			Row row=null;
			addIdentacion();
		
			for (  int i = 0; i < n ; i++ ){
			   row = symbolTable.getRow(i);
			    token=row.getToken();
			    type =row.getType();
			    lexeme=row.getLexeme();
			    if ( token.compareTo("identifier")==0 )
			            if ( type.compareTo("long")==0 )
			            	fstream.writeArchivo(lexeme + "    dd      "  +  "   0   " +    ";      identificador  long");
			            else
			            	fstream.writeArchivo(lexeme + "    dw      "  +  "   0   " +    ";      identificador  uint");
			    if ( token.compareTo("string")==0 ){
			            
			            strAddr=Integer.toString(i);
			    		strNum="string"+ strAddr;
			    		fstream.writeArchivo(strNum    +   "       db      "       +   "'" + lexeme + "',10,13,'$'");
			          }
			   }
			fstream.writeArchivo("\n;          Chequeos en tiempo de ejecución:\n");
			
			fstream.writeArchivo("runtimeExceptionDivByZero            db      '[Error]: No se puede dividir por 0.',10,13,'$'");
			fstream.writeArchivo("runtimeExceptionSubOp           db      '[Error]: Sustracción  fuera de rango.',10,13,'$'");
		}
		
//------------------------------------------------------------
		
		public void generarCodigo(){
			generarDataSeg();
			generarCodeSeg();
			fstream.endWrite();
		}

//------------------------------------------------------------
		public void generarIDiv(Third third){

			
			// mov  eaux,   a_0_0
			
			
			boolean isExtended=false;
			Register regDndL=new Register("ax");
			Register regDndH=new Register("dx");
			Register regDsr=new Register("bx");
			String opCode = "div";
			if ( third != null){
			    if ( (third.getType()).compareTo("long")==0 ){
			            isExtended=true;
			            opCode="idiv";
			           }
			     Operand  opL = third.getLeftOp();
			     Operand  opR = third.getRightOp();
			     regDndL.setExtended(isExtended);
			     regDndH.setExtended(isExtended);
			     regDsr.setExtended(isExtended);
			     fstream.writeArchivo(" xor  " +  regDndH.getName() + ",  " + regDndH.getName() );
			     if ( (!opL.isRegistro()) && (!opR.isRegistro()) ){
			    	 fstream.writeArchivo( " mov  " + regDndL.getName() + ",  "  +  opL.getValue());
			    	 fstream.writeArchivo( " mov  " + regDsr.getName() + ",  "  +  opR.getValue());
			    	 runtimeTestDivByZero(regDsr);
			    	 fstream.writeArchivo(opCode + "    "    +  regDsr.getName());
			     }
			     if ( (!opL.isRegistro()) && (opR.isRegistro()) ){
			    	 
			    	 fstream.writeArchivo(" pop  "  + regDsr.getName()  );
			         runtimeTestDivByZero(regDsr);
			         fstream.writeArchivo(" mov  "  + regDndL.getName() +  ",   "  + opL.getValue());
			         fstream.writeArchivo(opCode + "    "    +  regDsr.getName());
			     }
			     if ( (opL.isRegistro()) && (!opR.isRegistro()) ){
			    	   fstream.writeArchivo(" mov  "  + regDsr.getName() +  ",   "  + opR.getValue());
			           runtimeTestDivByZero(regDsr);
			           fstream.writeArchivo(" pop  "  + regDndL.getName()  );
			           fstream.writeArchivo(opCode + "    "    +  regDsr.getName());
			     }
			     if ( (opL.isRegistro()) && (opR.isRegistro()) ){
			    	   fstream.writeArchivo(" pop  "  + regDsr.getName()  );
			           runtimeTestDivByZero(regDsr);
			           fstream.writeArchivo(" pop  "  + regDndL.getName()  );
			           fstream.writeArchivo(opCode + "    "    +  regDsr.getName());
			     }
			     	fstream.writeArchivo("push "   + regDndL.getName());
			}
}
//------------------------------------------------------------

void generarIMul(Third third){

		boolean  isExtended=false;
		String opCode="mul";
		Register regMulH = new Register("dx");
		Register regMulL= new Register("ax");
		Register regMdr= new Register("cx");
		
		if ( third != null){
		if ( third.getType().compareTo("long")==0){
		            isExtended=true;
		            opCode="imul";
		            }
		    Operand  opL=third.getLeftOp();
		    Operand  opR=third.getRightOp();
		    regMulH.setExtended(isExtended);
		    regMulL.setExtended(isExtended);
		    regMdr.setExtended(isExtended);
		    
		    
		    
		    if ((!opL.isRegistro()) && (!opR.isRegistro() ) ) {          //  (/,VARIABLE,VARIABLE)
		    	fstream.writeArchivo("mov  " + regMulL.getName() + ", "  + opL.getValue() );
		    	fstream.writeArchivo("mov  " + regMdr.getName() + ",  " + opR.getValue() );
		    	fstream.writeArchivo(opCode + "    "    +  regMdr.getName() );
		    }
		    if ( (!opL.isRegistro()) && ( opR.isRegistro()) ){
		    	fstream.writeArchivo(" pop  "  + regMdr.getName()  );
		    	fstream.writeArchivo(" mov  "  + regMulL.getName() +  ",   "  + opL.getValue());
		    	fstream.writeArchivo(opCode + "    "    +  regMdr.getName());
		     }
		    if ( (opL.isRegistro()) && ( !opR.isRegistro()) ){
		    	fstream.writeArchivo(" pop  "  + regMulL.getName() );
		    	fstream.writeArchivo(" mov  "  + regMdr.getName() +  ",   "  + opR.getValue());
		    	fstream.writeArchivo(opCode + "    "    +  regMdr.getName() );
		     }
		    if ( (opL.isRegistro()) && ( opR.isRegistro()) ){
		    	fstream.writeArchivo(" pop  "  + regMdr.getName()  );
		    	fstream.writeArchivo(" pop  "  + regMulL.getName()  );
		    	fstream.writeArchivo(opCode + "    "    +  regMdr.getName() );
		     }
		    	fstream.writeArchivo("push "   + regMulL.getName());
		}
}
//-----------------------------------------------------
	public void tolongRegister(Third third){
		
		Register regAux=new Register("cx");
		Register regSrc=new Register("bx");
			if ( third != null ){
			        Operand opL = third.getRightOp();
			        
			        
			        /////
			        /*
			        Operand opR = third.getLeftOp();
			        if (opR != null){
			        	System.out.println(" OPERANDO IZQIOERDP TOLONG " + opR.getValue()  + "  ");
				        showThird(third);

			        }
			        /////
			        */
			        if (  opL.isRegistro() ){
			        	fstream.writeArchivo(";                                         tolong register ");
			        	fstream.writeArchivo(" pop  " + regSrc.getName()  + ";  sacando el registro a extender");
                        if  (opL.getType().compareTo("uint")==0  ) {
                        	fstream.writeArchivo(" ;                        el registro es uint.");
                        	regAux.setExtended();
                        	fstream.writeArchivo(" xor    " + regAux.getName()   + ",   " + regAux.getName());
			                regAux.setExtended(false);
			                fstream.writeArchivo(" mov    "   +  regAux.getName()  +  ", " +  regSrc.getName());
			                regSrc.setExtended();
			                regAux.setExtended();
			                fstream.writeArchivo("mov " + regSrc.getName() + ",  " + regAux.getName());
                        	}
                        fstream.writeArchivo(" push " + regSrc.getName() );
			            }
			}
		
	
	}

//-----------------------------------------------------
void tolongMemory(Third third){

	Register regAux=new Register("bx");
	regAux.setExtended();
	Third  refL;
	String regTempL;
	if ( third != null){
	    Operand  opL = third.getRightOp();
	    if (  !opL.isRegistro() ) {
	    	fstream.writeArchivo(";           tolong variable ");
	              if  (  opL.getType().compareTo("uint")==0  ) {
	            	  fstream.writeArchivo(" ;                        la varaible es uint.");
	            	  fstream.writeArchivo(" xor " + regAux.getName()   + ",  " + regAux.getName() );
	                            regAux.setExtended(false);
	                           }
	           fstream.writeArchivo(" mov " + regAux.getName()  +  ", " + opL.getValue() );
	           regAux.setExtended();
	           fstream.writeArchivo(" push " + regAux.getName() );
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
		Register regSrc=new Register("bx");
		Register regDest=new Register("cx");
		if ( third != null){
		    if ( (third.getType()).compareTo("long")==0)
		            isExtended=true;
		    regSrc.setExtended( isExtended);
		    regDest.setExtended(isExtended);
		    fstream.writeArchivo(" ;                                                        suma.");
		    Operand  opL = third.getLeftOp();
		    Operand  opR = third.getRightOp();
		    if (( !opL.isRegistro()) && (!opR.isRegistro() ) ) {
		    	fstream.writeArchivo(" mov " + regDest.getName() +  ", " + opL.getValue() );
		    	fstream.writeArchivo(" add "+  regDest.getName() +  ", " + opR.getValue() );
		    }
		    if ( ( !opL.isRegistro() ) && ( opR.isRegistro() ) ) {// ( + ,VARIABLE, REGISTRO )
		    		fstream.writeArchivo( " pop  " +  regDest.getName() );
		    		fstream.writeArchivo( " add   " + regDest.getName() + ", " + opL.getValue() );
		    }
		    if (( opL.isRegistro()) && (!opR.isRegistro() ) ) {
		    		fstream.writeArchivo( " pop  " +  regDest.getName() );
		    		fstream.writeArchivo( " add   " + regDest.getName() + ", " + opR.getValue() );
		    }
		    if (( opL.isRegistro()) && (opR.isRegistro() ) ) {// ( + ,REGISTRO,REGISTRO )
		    		fstream.writeArchivo(" pop " + regSrc.getName());
		    		fstream.writeArchivo(" pop " + regDest.getName());
		    		fstream.writeArchivo("add  " + regDest.getName() + ", " + regSrc.getName() );
		   }
		
		    	fstream.writeArchivo("push " + regDest.getName() );
		    	fstream.writeArchivo( "                            ; ( + , " + regDest.getName()  + ",  " +  regSrc.getName()  +")" );
		
		  }
}
//------------------------------------------------------------
public void generarISub(Third third){

	boolean  isExtended=false;
	Register regSrc=new Register("bx");
	Register regDest=new Register("cx");
	if ( third != null){
	    if ( (third.getType()).compareTo("long")==0)
	            isExtended=true;
	    regSrc.setExtended( isExtended);
	    regDest.setExtended(isExtended);
	    fstream.writeArchivo(" ;                                                        Resta.");
	    Operand  opL = third.getLeftOp();
	    Operand  opR = third.getRightOp();
	    if (( !opL.isRegistro()) && (!opR.isRegistro() ) ) {
	              fstream.writeArchivo(" mov " + regDest.getName() +  ", " + opL.getValue() );
	              fstream.writeArchivo(" sub "+  regDest.getName() +  ", " + opR.getValue() );
	    }
	    if (( !opL.isRegistro()) && ( opR.isRegistro() ) ) {
	              fstream.writeArchivo(" pop  " + regSrc.getName());
	              fstream.writeArchivo(" mov  " + regDest.getName() + ",  " + opL.getValue());
	              fstream.writeArchivo(" sub "+   regDest.getName() +  ", " + regSrc.getName() );
	    }
	    if ((  opL.isRegistro()) && ( !opR.isRegistro() ) ) {
	              fstream.writeArchivo(" pop   "   +  regDest.getName());
	              fstream.writeArchivo(" sub "+  regDest.getName() +  ", " + opR.getValue() );
	    }
	    if (( opL.isRegistro()) && ( opR.isRegistro() ) ) {
	              fstream.writeArchivo(" pop   "    +   regSrc.getName());
	              fstream.writeArchivo(" pop   "    +   regDest.getName());
	              fstream.writeArchivo(" sub "+  regDest.getName() +  ", " + regSrc.getName() );
	    }
	    runtimeTestSub(third,regDest.getName());
	    fstream.writeArchivo( " push " + regDest.getName());
	     }
}
//------------------------------------------------------------
public void generarIAssign(Third third){

	boolean isExtended=false;
	Register regAux=new Register("dx");
	Register regSrc=new Register("ax");
	if ( third != null){
	    if ( (third.getType()).compareTo("long")==0)
	            isExtended=true;
	    regSrc.setExtended(isExtended);
	    regAux.setExtended(isExtended);
	    Operand  opL = third.getLeftOp();
	    Operand  opR = third.getRightOp();
	    if (( !opL.isRegistro()) && (!opR.isRegistro() ) ) {//  (:=,VARIABLE,VARIABLE)
	
	            fstream.writeArchivo(" mov  " + regAux.getName() + ",  " + opR.getValue());
	            fstream.writeArchivo(" mov  " + opL.getValue()  + ",  "   + regAux.getName());
	          }
	    if (( !opL.isRegistro()) && (  opR.isRegistro() ) ) {
	                    fstream.writeArchivo(" pop   " + regAux.getName() );
	                    fstream.writeArchivo(" mov  " +  opL.getValue() + ",  " + regAux.getName()   );
	         }
	}


}
//------------------------------------------------------------
public void generarPrint(Third third){

		String strAddr="";
		String strAux="";
		int i=0;
		if (third != null){
	            i=this.symbolTable.indexSymbol((third.getRightOp()).getValue());
	            strAux = Integer.toString(i);
	            strNum="String"+ strAux;
	            fstream.writeArchivo("mov ah, 09");
	            fstream.writeArchivo("lea dx, "+ strNum);
	            fstream.writeArchivo("int 21h");
	
	}

}
//------------------------------------------------------------
public void generarCmp(Third third){

	Register regL=new Register("bx");
	Register regR=new Register("dx");
	
	myRegisters.setBusy(regL);
	myRegisters.setBusy(regR);
	boolean  isExtended=false;
	
	if ( third != null){
	    Operand opL = third.getLeftOp();
	    Operand opR = third.getRightOp();
	
	    if ( third.getType().compareTo("long")==0 )
	              isExtended=true;
	    regL.setExtended(isExtended);
	    regR.setExtended(isExtended);
	    if ( ( !opL.isRegistro() )  && ( !opR.isRegistro() ) ) { //  ( cmp, VARIABLE, VARIABLE )
	           fstream.writeArchivo (" mov " +   regL.getName() +  ",  "  + opL.getValue() );
	           fstream.writeArchivo (" cmp "  +  regL.getName() + ",  "  + opR.getValue() );
	      }
	    if ( ( !opL.isRegistro() )  && (opR.isRegistro()  ) ){ //   ( cmp, VARIABLE, REGISTRO  )
	           fstream.writeArchivo (" mov " +  regL.getName() +  ",  "  + opL.getValue() );
	           fstream.writeArchivo (" pop " +  regR.getName() );
	           fstream.writeArchivo (" cmp "  + regL.getName() + ",  " +  regR.getName() );
	           }
	    if ( ( opL.isRegistro() )  && ( !opR.isRegistro() ) ){ //  ( cmp, REGISTRO, VARIABLE  )
	            fstream.writeArchivo(" pop "  + regL.getName());
	            fstream.writeArchivo(" cmp "  + regL.getName() + ",   " + opR.getValue() ) ;
	     }
	    if ( ( opL.isRegistro() )  && ( opR.isRegistro()  ) ){ //   ( cmp, REGISTRO, REGISTRO  )
	            fstream.writeArchivo(" pop " + regR.getName());
	            fstream.writeArchivo(" pop " + regL.getName());
	            fstream.writeArchivo("cmp  " + regL.getName() + ", " + regL.getName());
	    }
  }
}

//------------------------------------------------------------
void generarFBranch(Third third){
		
		String cond, labelJmp,jmpType;
		
		if ( ( third != null)  ) {
		
		    cond = third.getOpPrevious();
		    labelJmp= third.getLabelSrc();
		    jmpType = this.getJmp(third.getType(),cond);
		    fstream.writeArchivo(jmpType + "    " + labelJmp);
		   
		}
}
//------------------------------------------------------------	
	public void generarIBranch(Third third){
	
		String labelJmp,jmpType;
		if ( ( third !=null ) ) {
		    labelJmp = third.getLabelSrc();
		    jmpType = this.getJmp("sinTipo","incondicional");
		    fstream.writeArchivo(jmpType +  "    " + labelJmp);
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
	
		fstream.writeArchivo("cmp  " + registerDsr.getName() + ", 0h");
		fstream.writeArchivo("jz errDivByZero");

}
//------------------------------------------------------------
void runtimeTestSub(Third third,String registerOp){
	

	if ( ( third != null) && ( third.getType().compareTo("uint")==0 ) ){
	    fstream.writeArchivo(" cmp  " + registerOp   +  ",0h ");
	    fstream.writeArchivo("jl errSub");
	}

}
//------------------------------------------------------------
/*********************************************************************************************************************************/
public void generarCodeSeg(){

	fstream.writeArchivo(".code");
	fstream.writeArchivo(".386");
	fstream.writeArchivo(".387");
	Vector<Third> myThirds = tGenerator.getTercetos();
	fstream.writeArchivo("start:");
	addIdentacion();
	addIdentacion();
	fstream.writeArchivo("                  mov ax,@data ");
	fstream.writeArchivo("                  mov ds,ax   ");
	fstream.writeArchivo("                  jmp start_code");
	quiteIdentacion();
	fstream.writeArchivo("errDivByZero:");
	addIdentacion();
	fstream.writeArchivo("                  mov ah,09 ");
	fstream.writeArchivo("                  mov dx, offset runtimeExceptionDivByZero");
	fstream.writeArchivo("                  int 21h ");
	fstream.writeArchivo("                  jmp exit_code ");
	quiteIdentacion();
	fstream.writeArchivo("errSub:");
	addIdentacion();
	fstream.writeArchivo("                  mov ah,09 ");
	fstream.writeArchivo("                  mov dx, offset runtimeExceptionSubOp");
	fstream.writeArchivo("                  int 21h ");
	fstream.writeArchivo("                  jmp exit_code ");
	fstream.writeArchivo("start_code:");
	//this.tGenerator.showTercetos();
	Third  temp;
	String labelDestiny;
	if ( myThirds != null ){
	    tGenerator.labeler("exit_code");
	    int nThirds=myThirds.size();
	    for (  int index=0;index < nThirds; index++)  {
	
	          fstream.writeArchivo("\n");
	
	          temp =  myThirds.get(index);
	          
	          ///////
	          
	          showThird(temp);
	          ///////
	          
	          if ( temp!=null) { 
	        	  labelDestiny = temp.getLabelDst(); 
	        	  if ( (labelDestiny!=null ) && (!labelDestiny.isEmpty()) ) 
	        		  fstream.writeArchivo(labelDestiny + ":");  
	        	  }
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
			fstream.writeArchivo("\n");
			quiteIdentacion();
			fstream.writeArchivo("exit_code:");
			addIdentacion();
			fstream.writeArchivo("                  mov ah,04ch");
			fstream.writeArchivo("                  mov al,00h");
			fstream.writeArchivo("                  int 21h");
			quiteIdentacion();
			fstream.writeArchivo("end start  ");
			fstream.writeArchivo("\n  ");
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
         	if (Integer.parseInt(i)==0){
         		System.out.println("");
         	}
         	String message="\n " + i  +  "  ( " + (temp).getOperator() + " ,  " +  left + " ,  " + right + " )- "  + temp.getType() + "\n";
         	Lexer.showMessage(message);
         	message="\n TERCETO -  "  + temp.getOperator()  + " \n "  + 	temp.getLeftOp() + "  \n " + temp.getRightOp()  + "\n";
            
         	Lexer.showMessage(message);
}
//------------------------------------------------------------

}
