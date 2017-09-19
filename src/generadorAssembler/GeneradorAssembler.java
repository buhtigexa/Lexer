package generadorAssembler;

import java.util.ArrayList;

import generadorCodigo.GeneradorTercetos;
import generadorCodigo.Operando;
import generadorCodigo.Terceto;
import symboltable.Row;
import symboltable.SymbolTable;
import utils.MyFStream;

public class GeneradorAssembler {


	 int countAux;
     String vaAux;                 
     int noConst;
     String aux;
     int noString;
     String strConst;
     String strNum;
     GeneradorTercetos generadorTercetos;
     SymbolTable symbolTable;
     BancoRegistros bancoRegistros;
     MyFStream fstream;
     int identation;
     
	public GeneradorAssembler(MyFStream fstream, GeneradorTercetos tg, SymbolTable st) {

		noConst = 0;
        aux="";
        noString = 0;
        strConst="";
        strNum="";

        generadorTercetos=tg;
        symbolTable = st;
        identation = 0;
        bancoRegistros= new BancoRegistros();
        this.fstream=fstream;

        Register bx=new Register("bx");
        Register cx=new Register("cx");
        Register dx=new Register("dx");
        Register ax=new Register("ax");


        bancoRegistros.addRegister(bx);
        bancoRegistros.addRegister(cx);

        bancoRegistros.addRegister(dx);
        bancoRegistros.addRegister(ax);
        countAux=0;


}
//------------------------------------------------------------

public String getAuxVar(boolean isExtended){

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
//------------------------------------------------------------

void writeArchivo(String txt) {
        String tabs = "";
        for ( int i = 0; i < identation; i++)
                tabs += "    ";
        //fileAsm << tabs + txt << endl;
        fstream.write(tabs + txt);
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

void generarDataSeg(){
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
     Row fila=null;
     addIdentacion();
     writeArchivo("; DATOS DE LA TABLA DE SIMBOLOS ");
        for (int i = 0; i < n ; i++ ){
                fila = symbolTable.getRow(i);
                token=fila.getToken();
                type =fila.getType();
                lexeme=fila.getLexeme();
                if ( token.compareTo("variable")==0 )
                        if ( type.compareTo("long")==0 )
                                writeArchivo(lexeme + "    dd      "  +  "   0   " +    ";      identificador  long");
                        else
                                writeArchivo(lexeme + "    dw      "  +  "   0   " +    ";      identificador  uint");
                if ( token.compareTo("string")==0 ){
                        strAddr=Integer.toString(i);
                        strNum="string"+ strAddr;
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
        fstream.endWrite();

}
/***********************************************************************************/
void generarIDiv(Terceto terceto){

        boolean isExtended=false;
        Register regDndL=new Register("ax");
        Register regDndH=new Register("dx");
        Register regDsr=new Register("bx");
        String opCode = "div";
        if ( terceto != null ){
                if ( (terceto.getType()).compareTo("long")==0 ){
                        isExtended=true;
                        opCode="idiv";
                        }
                 Operando opL = (Operando) terceto.opI;
                 Operando opR = (Operando) terceto.opD;
                 regDndL.setExtended(isExtended);
                 regDndH.setExtended(isExtended);
                 regDsr.setExtended(isExtended);
                 writeArchivo(" xor  " +  regDndH.getName() + ",  " + regDndH.getName() );
                 if ( (!opL.isReferencia()) && (!opR.isReferencia()) ){
                        writeArchivo( " mov  " + regDndL.getName() + ",  "  +  ((Row)opL).getLexeme());
                        writeArchivo( " mov  " + regDsr.getName() + ",  "  +  ((Row)opR).getLexeme());
                        runtimeTestDivByZero(regDsr);
                        writeArchivo(opCode + "    "    +  regDsr.getName());
                 }
                 if ( (!opL.isReferencia()) && (opR.isReferencia()) ){
                        writeArchivo(" pop  "  + regDsr.getName()  );
                        runtimeTestDivByZero(regDsr);
                        writeArchivo(" mov  "  + regDndL.getName() +  ",   "  + ((Row)opL).getLexeme() );
                        writeArchivo(opCode + "    "    +  regDsr.getName());
                 }
                 if ( (opL.isReferencia()) && (!opR.isReferencia()) ){
                       writeArchivo(" mov  "  + regDsr.getName() +  ",   "  + ((Row)opR).getLexeme() );
                       runtimeTestDivByZero(regDsr);
                       writeArchivo(" pop  "  + regDndL.getName()  );
                       writeArchivo(opCode + "    "    +  regDsr.getName());
                 }
                 if ( (opL.isReferencia()) && (opR.isReferencia()) ){
                       writeArchivo(" pop  "  + regDsr.getName()  );
                       runtimeTestDivByZero(regDsr);
                       writeArchivo(" pop  "  + regDndL.getName()  );
                       writeArchivo(opCode + "    "    +  regDsr.getName());
                 }
                 writeArchivo("push "   + regDndL.getName());
        }
}
//------------------------------------------------------------

public void generarIMul(Terceto terceto){

      boolean isExtended=false;
      String opCode="mul";
      Register regMulH=new Register("dx");
      Register regMulL=new Register("ax");
      Register regMdr =new Register("cx");
      if ( terceto != null ){
        if ( terceto.getType().compareTo("long")==0){
                        isExtended=true;
                        opCode="imul";
                        }
                Operando opL= (Operando)terceto.opI;
                Operando opR= (Operando)terceto.opD;
                regMulH.setExtended(isExtended);
                regMulL.setExtended(isExtended);
                regMdr.setExtended(isExtended);
                if ((!opL.isReferencia()) && (!opR.isReferencia() ) ) {          
                	
                        writeArchivo("mov  " + regMulL.getName() + ", "  + ((Row)opL).getLexeme() );
                        writeArchivo("mov  " + regMdr.getName() + ",  " + ((Row)opR).getLexeme() );
                        writeArchivo(opCode + "    "    +  regMdr.getName() );
                }
                
                if ( (!opL.isReferencia()) && ( opR.isReferencia()) ){
                        writeArchivo(" pop  "  + regMdr.getName()  );
                        writeArchivo(" mov  "  + regMulL.getName() +  ",   "  + ((Row)opL).getLexeme() );
                        writeArchivo(opCode + "    "    +  regMdr.getName());
                 }
                if ( (opL.isReferencia()) && ( !opR.isReferencia()) ){
                        writeArchivo(" pop  "  + regMulL.getName() );
                        writeArchivo(" mov  "  + regMdr.getName() +  ",   "  + ((Row)opR).getLexeme());
                        writeArchivo(opCode + "    "    +  regMdr.getName() );
                 }
                if ( (opL.isReferencia()) && ( opR.isReferencia()) ){
                        writeArchivo(" pop  "  + regMdr.getName()  );
                        writeArchivo(" pop  "  + regMulL.getName()  );
                        writeArchivo(opCode + "    "    +  regMdr.getName() );
                 }
               writeArchivo("push "   + regMulL.getName());
      }
}
//-----------------------------------------------------
public void tolongRegister(Terceto terceto){

        Register regAux=new Register("cx");
        Register regSrc=new Register("bx");
        if ( terceto != null ){
                Operando opL = (Operando)terceto.opD;
                if (  opL.isReferencia() )
                       {writeArchivo(";    // TOLONG REGISTER ");
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
public void tolongMemory(Terceto terceto){

        Register regAux=new Register("bx");
        regAux.setExtended();
        Terceto refL;
        String regTempL;
        if ( terceto != null ){
                Operando opL = (Operando)terceto.opD;
                if (  !opL.isReferencia() ) {
                        writeArchivo(";  // TOLONG VARIABLE ");
                          if  (  opL.getType().compareTo("uint")==0  ) {
                                        writeArchivo(" ;                        la varaible es uint.");
                                        writeArchivo(" xor " + regAux.getName()   + ",  " + regAux.getName() );
                                        regAux.setExtended(false);
                                       }
                       writeArchivo(" mov " + regAux.getName()  +  ", " + ((Row)opL).getLexeme() );
                       regAux.setExtended();
                       writeArchivo(" push " + regAux.getName() );
                      }
               }
  }
//-----------------------------------------------------
void generarTolong(Terceto terceto){
         tolongRegister(terceto);
         tolongMemory(terceto);
}
//-----------------------------------------------------
public void generarIAdd(Terceto terceto){

      boolean isExtended=false;
      Register regSrc=new Register("bx");
      Register regDest=new Register("cx");
      if ( terceto != null ){
                if ( (terceto.getType()).compareTo("long")==0)
                        isExtended=true;
                regSrc.setExtended( isExtended);
                regDest.setExtended(isExtended);
                writeArchivo(" ;  // SUMA.");
                Operando opL = (Operando)terceto.opI;
                Operando opR = (Operando)terceto.opD;
                if (( !opL.isReferencia()) && (!opR.isReferencia() ) ) {
                          writeArchivo(" mov " + regDest.getName() +  ", " + ((Row)opL).getLexeme() );
                          writeArchivo(" add "+  regDest.getName() +  ", " + ((Row)opR).getLexeme() );
                }
                if ( ( !opL.isReferencia() ) && ( opR.isReferencia() ) ) {// ( + ,VARIABLE, REGISTRO )
                          writeArchivo( " pop  " +  regDest.getName() );
                          writeArchivo( " add   " + regDest.getName() + ", " + ((Row)opL).getLexeme() );
                }
                if (( opL.isReferencia()) && (!opR.isReferencia() ) ) {
                          writeArchivo( " pop  " +  regDest.getName() );
                          writeArchivo( " add   " + regDest.getName() + ", " + ((Row)opR).getLexeme() );
                }
                if (( opL.isReferencia()) && (opR.isReferencia() ) ) {// ( + ,REGISTRO,REGISTRO )
                           writeArchivo(" pop " + regSrc.getName());
                           writeArchivo(" pop " + regDest.getName());
                           writeArchivo("add  " + regDest.getName() + ", " + regSrc.getName() );
               }

            writeArchivo("push " + regDest.getName() );
            //writeArchivo( "                            ; ( + , " + regDest.getName()  + ",  " +  regSrc.getName()  +")" );

          }
}
//------------------------------------------------------------
void generarISub(Terceto terceto){

      boolean isExtended=false;
      Register regSrc=new Register("bx");
      Register regDest=new Register("cx");
      if ( terceto != null ){
                if ( (terceto.getType()).compareTo("long")==0)
                        isExtended=true;
                regSrc.setExtended( isExtended);
                regDest.setExtended(isExtended);
                writeArchivo(" ;    // RESTA.");
                Operando opL = (Operando)terceto.opI;
                Operando opR = (Operando)terceto.opD;
                if (( !opL.isReferencia()) && (!opR.isReferencia() ) ) {
                          writeArchivo(" mov " + regDest.getName() +  ", " + ((Row)opL).getLexeme() );
                          writeArchivo(" sub "+  regDest.getName() +  ", " + ((Row)opR).getLexeme() );
                }
                if (( !opL.isReferencia()) && ( opR.isReferencia() ) ) {
                          writeArchivo(" pop  " + regSrc.getName());
                          writeArchivo(" mov  " + regDest.getName() + ",  " + ((Row)opL).getLexeme());
                          writeArchivo(" sub "+   regDest.getName() +  ", " + regSrc.getName() );
                }
                if ((  opL.isReferencia()) && ( !opR.isReferencia() ) ) {
                          writeArchivo(" pop   "   +  regDest.getName());
                          writeArchivo(" sub "+  regDest.getName() +  ", " + ((Row)opR).getLexeme() );
                }
                if (( opL.isReferencia()) && ( opR.isReferencia() ) ) {
                          writeArchivo(" pop   "    +   regSrc.getName());
                          writeArchivo(" pop   "    +   regDest.getName());
                          writeArchivo(" sub "+  regDest.getName() +  ", " + regSrc.getName() );
                }
                runtimeTestSub(terceto,regDest.getName());
                writeArchivo( " push " + regDest.getName());
                 }
}
//------------------------------------------------------------
void generarIAssign(Terceto terceto){

      boolean isExtended=false;
      Register regAux=new Register("dx");
      Register regSrc=new Register("ax");
      if ( terceto != null ){
                if ( (terceto.getType()).compareTo("long")==0)
                        isExtended=true;
                regSrc.setExtended(isExtended);
                regAux.setExtended(isExtended);
                Operando opL = (Operando)terceto.opI;
                Operando opR = (Operando)terceto.opD;
                if (( !opL.isReferencia()) && (!opR.isReferencia() ) ) {//  (:=,VARIABLE,VARIABLE)

                        writeArchivo(" mov  " + regAux.getName() + ",  " + ((Row)opR).getLexeme() );
                        writeArchivo(" mov  " + ((Row)opL).getLexeme()  + ",  "   + regAux.getName());
                      }
                if (( !opL.isReferencia()) && (  opR.isReferencia() ) ) {
                                writeArchivo(" pop   " + regAux.getName() );
                                writeArchivo(" mov  " +  ((Row)opL).getLexeme() + ",  " + regAux.getName()   );
                     }
      }


}
//------------------------------------------------------------
public void generarPrint(Terceto terceto){

   String strAddr;
   String strAux;
   int i=0;
   if (terceto != null ){
                        i=this.symbolTable.indexSymbol(((Row)terceto.opD).getLexeme());
                        strAux = (Integer.toString(i));
                        strNum="string"+ strAux;
                        writeArchivo("mov ah, 09");
                        writeArchivo("lea dx, "+ strNum);
                        writeArchivo("int 21h");

        }

}
//------------------------------------------------------------
public void generarCmp(Terceto terceto){

       Register regL=new Register("bx");
       Register regR=new Register("dx");

       bancoRegistros.setBusy(regL);
       bancoRegistros.setBusy(regR);
       
       boolean isExtended=false;
       if ( terceto != null ){
                Operando opL = (Operando)terceto.opI;
                Operando opR = (Operando)terceto.opD;

                if ( terceto.getType().compareTo("long")==0 ){
                          isExtended=true;
                }
                
                regL.setExtended(isExtended);
                regR.setExtended(isExtended);
                
                if ( ( !opL.isReferencia() )  && ( !opR.isReferencia() ) ) { //  ( cmp, VARIABLE, VARIABLE )
                        writeArchivo (" mov " +   regL.getName() +  ",  "  + ((Row)opL).getLexeme() );
                        writeArchivo (" cmp "  +  regL.getName() + ",  "  + ((Row)opR).getLexeme());
                  }
                if ( ( !opL.isReferencia() )  && (opR.isReferencia()  ) ){ //   ( cmp, VARIABLE, REGISTRO  )
                        writeArchivo (" mov " +  regL.getName() +  ",  "  + ((Row)opL).getLexeme() );
                        writeArchivo (" pop " +  regR.getName() );
                        writeArchivo (" cmp "  + regL.getName() + ",  " +  regR.getName() );
                       }
                if ( ( opL.isReferencia() )  && ( !opR.isReferencia() ) ){ //  ( cmp, REGISTRO, VARIABLE  )
                        writeArchivo(" pop "  + regL.getName());
                        writeArchivo(" cmp "  + regL.getName() + ",   " + ((Row)opR).getLexeme() ) ;
                 }
                if ( ( opL.isReferencia() )  && ( opR.isReferencia()  ) ){ //   ( cmp, REGISTRO, REGISTRO  )
                        writeArchivo(" pop " + regR.getName());
                        writeArchivo(" pop " + regL.getName());
                        writeArchivo("cmp  " + regL.getName() + ", " + regL.getName());
                }
             }

}
//------------------------------------------------------------
void generarFBranch(Terceto terceto){

        String cond, labelJmp,jmpType;

        if ( ( terceto != null )  ) {

                cond = terceto.operadorAnterior;
                labelJmp= terceto.labelSrc;
                jmpType = this.getJmp(terceto.getType(),cond);
                writeArchivo(jmpType + "    " + labelJmp);
               
        }
}
//------------------------------------------------------------
void generarIBranch(Terceto terceto){
      String labelJmp,jmpType;
      if ( ( terceto !=null ) ) {
                labelJmp = terceto.labelSrc;
                jmpType = this.getJmp("sinTipo","incondicional");
                writeArchivo(jmpType +  "    " + labelJmp);
             }
}
//------------------------------------------------------------
public String getJmp(String type, String cond){
        String tempJ="jmp";
        //if ( type.compareTo("uint")==0){
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
        //}
         //if ( type.compareTo("long")==0){
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

        //}
      return tempJ;
}
//------------------------------------------------------------
void runtimeTestDivByZero(Register registerDsr){
        writeArchivo("cmp  " + registerDsr.getName() + ", 0h");
        writeArchivo("jz errDivByZero");
}
//------------------------------------------------------------
void runtimeTestSub(Terceto terceto,String registerOp){
        if ( ( terceto != null ) && ( terceto.getType().compareTo("uint")==0 ) ){
                writeArchivo(" cmp  " + registerOp   +  ",0h ");
                writeArchivo("jl errSub");
        }

}
//------------------------------------------------------------
/*********************************************************************************************************************************/
void generarCodeSeg(){

        writeArchivo(".code");
        writeArchivo(".386");
        writeArchivo(".387");
        ArrayList<Terceto> myTercetos = generadorTercetos.tercetos;
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
        //System.out.println(generadorTercetos);
        Terceto temp;
        String labelDestiny;
        if ( myTercetos != null ){
                generadorTercetos.etiquetador("exit_code");
                 int nTercetos=myTercetos.size();
                for (  int index=0;index < nTercetos; index++)  {

                      writeArchivo("\n");

                      temp =  myTercetos.get(index);
                      
                      	/****** solo impresión */
                      //writeArchivo("; " + temp.toString());
                      
                      
                      /*** solo impresion */ 
                      if ( temp!=null) { 
                    	  labelDestiny = temp.labelDst;
                      		if ( !labelDestiny.isEmpty() ) 
                      			writeArchivo(labelDestiny + ":");  
                      	}
                      if ( ( temp.operador).compareTo("+")==0 )
                           generarIAdd(temp);
                      if ( (temp.operador).compareTo(":=")==0 )
                           generarIAssign(temp);
                      if ( (temp.operador).compareTo("-")==0 )
                           generarISub(temp);
                      if ( (temp.operador).compareTo("*")==0 )
                           generarIMul(temp);
                      if ( (temp.operador).compareTo("print")==0 )
                           generarPrint(temp);
                      if ( (temp.operador).compareTo("/")==0 )
                           generarIDiv(temp);
                      if ( (temp.operador).compareTo("tolong")==0 )
                           generarTolong(temp);
                      if (( ( temp.operador.compareTo(">")==0  ) || ( temp.operador.compareTo("<")==0  ) )||
                        ( ( temp.operador.compareTo(">=")==0 ) || ( temp.operador.compareTo("<=")==0 ) )||
                        ( ( temp.operador.compareTo("=")==0  ) || ( temp.operador.compareTo("<>")==0 )  )
                        )
                          generarCmp(temp);

                      if ( (temp.operador).compareTo("BF")==0 ){
                                generarFBranch(temp);
                      }
                      if ( (temp.operador).compareTo("BI")==0 )
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

}
