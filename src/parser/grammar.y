/******************************************************************************************************************



                                        Dise�o de Compiladores 1

        Temas:
              E1-E2     :       B,F,H,I,L,M.
              E3        :       Conversiones Explicitas,Tercetos.
                                -------------------------------------------
                                            |    Permitido    |   Prohibida
                                            |                 |
                                uint long   |    uint a long  |   long a uint
                                            |    tolong()     |   touint()
                                -------------------------------------------
               E4       :       Errores en tiempo de ejecucion c) Divisi�n por 0.
                                                               d) Resultado negativo en resta de datos sin signo.



        Grupo: 32.

        Alumno:Rodriguez, Marcelo Javier.

        e-mail:marcelojavierrodriguez@gmail.com






******************************************************************************************************************/
%{

package parser;

import java.util.Hashtable;
import automaton.Lexer;
import symboltable.*;
import codeGenerator.*;
import utils.*;


%}

%token T_IDENTIFIER
       T_CONST
       T_UINT
       T_LONG
       T_STRING
       T_RW_IF
       T_RW_ELSE
       T_RW_DO
       T_RW_PRINT
       T_RW_WHILE
       T_RW_TOLONG
       T_RW_LONG
       T_RW_UINT
       T_GT
       T_EQ
       T_LT
       T_BEQ
       T_GEQ
       T_ASSIGN
       T_DISTINCT
       T_PLUS_PLUS
       T_ENDOFFILE


%left '+' '-'
%left '*' '/'

%start programa 

%%
programa:
        | '{' declaraciones  conjunto_sentencias'}' T_ENDOFFILE 
        | '{'declaraciones'}' T_ENDOFFILE
        | '{' conjunto_sentencias '}' T_ENDOFFILE
        | declaracion_sentencia T_ENDOFFILE
        | declaraciones conjunto_sentencias T_ENDOFFILE   
        ;


declaracion_sentencia :declaracion 
                      | sentencia  
                      ;

declaraciones: declaracion
              |declaraciones declaracion
              ;

declaracion: d_variable  
           ;


d_variable : tipo lista_identificadores ';'
           | tipo lista_identificadores error 
           | tipo error  
           ;



lista_identificadores   : T_IDENTIFIER                            {  updateDeclaration(((Row)$1.obj),tipo_identificador);    }

                        | lista_identificadores ',' T_IDENTIFIER  {  updateDeclaration((Row)($3.obj),tipo_identificador);    }   

                        | lista_identificadores T_IDENTIFIER       
                        ;


conjunto_sentencias : sentencia
                    | conjunto_sentencias   {
                                              decorator.nroAnidamiento = decorator.nroAnidamiento + 1;
                                              if ( decorator.nroAnidamiento == 1 ) {
                                                   decorator.nroAmbito  = aux;
                                                   decorator.nroAmbito++;

                                                   }
                                             } '{' ambito '}' { if ( decorator.nroAnidamiento >= 2  )
                                                                     syntaxError("No se permite anidamiento de ambitos.");
                                                                     decorator.nroAnidamiento=decorator.nroAnidamiento - 1;
                                                                     if ( decorator.nroAnidamiento < 1 ) {
                                                                                    decorator.nroAnidamiento= 0;
                                                                                    aux = decorator.nroAmbito;
                                                                                    decorator.nroAmbito = 0;
                                                                                  }
                                                                     }

                    |   { syntaxError("Se esperan sentencias ejecutables antes de la declaraci�n de un �mbito.");
                          decorator.nroAnidamiento = decorator.nroAnidamiento + 1;
                                  if ( decorator.nroAnidamiento == 1 ){
                                                                   decorator.nroAmbito  = aux;
                                                                   decorator.nroAmbito ++;

                                                                }
                         } '{' ambito '}' { if ( decorator.nroAnidamiento >= 2  )
                                                            syntaxError("No se permite anidamiento de ambitos.");
                                                            decorator.nroAnidamiento=decorator.nroAnidamiento - 1;
                                                            if ( decorator.nroAnidamiento < 1 ){
                                                                                  decorator.nroAnidamiento = 0;
                                                                                  aux = decorator.nroAmbito;
                                                                                  decorator.nroAmbito = 0;
                                                              }


                                            }
                    | conjunto_sentencias sentencia
                    | conjunto_sentencias declaracion { syntaxError("Las declaraciones deben preceder a las sentencias ejecutables."); }
                    ;


sentencias : sentencia
           | sentencias sentencia
           ;


ambito  :  { informarSentencia("Declaraci�n de ambito.");} declaraciones conjunto_sentencias

        |  { informarSentencia("Declaraci�n de ambito.");} conjunto_sentencias

        |   error   /*{ syntaxError("Declarci�n de ambito erroneo");}*/
        ;


sentencia       : { informarSentencia("Sentencia IF."); } if
                | asignacion';'
                | do';'
                | impresion';'
                | asignacion                    {       syntaxError("Se espera ; al finalizar la sentencia.")  ;}
                | impresion                     {       syntaxError("Se espera ; al finalizar la sentencia.")  ;}
                | do                            {       syntaxError("Se espera ; al finalizar la sentencia.") ;}
                ;


asignacion      :        lado_izquierdo T_ASSIGN exp_ar      { thirdGenerator.generarTerceto(":=",false); }   


                |        lado_izquierdo T_EQ exp_ar             
                |        lado_izquierdo T_ASSIGN error
                |        lado_izquierdo T_EQ error              
                |        lado_izquierdo error                                   ;


if              :       T_RW_IF condicion            {  thirdGenerator.generarTerceto("BF",true);
                                                     }

                                         bloque       { thirdGenerator.completarSalto(true); }
                                                else


                |       T_RW_IF condicion error else  {     syntaxError("Sentencia IF:Se espera { en el bloque de sentencias.");}
                |       T_RW_IF error bloque    else       { syntaxError("Sentencia IF:Se espera ( ) ");}
                |       T_RW_IF error else
                ;



else            :       T_RW_ELSE                        {  
                                                            thirdGenerator.generarTerceto("BI",true);
                                                         }

                                   bloque               {
                                                          thirdGenerator.completarSalto(false);
                                                          thirdGenerator.showPila(thirdGenerator.getOperadores());
                                                        }

                |       T_RW_ELSE error                 { syntaxError("Sentencia IF-ELSE:Se espera sentencia o bloque de sentencias."); }

                |       error                           { syntaxError("Sentencia IF-ELSE:Se espera ELSE o el bloque de sentencias es erróneo.");}
                ;



condicion       :       '('comparacion')'            {
                                                       thirdGenerator.showPila(thirdGenerator.getOperadores());
                                                       thirdGenerator.desapilarOperando();
                                                     }

                |        '(' error ')'               {syntaxError("Se esperaba ( ) en lugar de { }");}
                ;


comparacion     :       comparacion sgn_cmp exp_ar   {  
                                                          Row row = (Row)$2.obj;
                                                          thirdGenerator.generarTerceto(row.getLexeme(),true);
                                                          thirdGenerator.showTercetos();
                                                       }


                |       comparacion error  exp_ar    { syntaxError("Operador de comparación no válido.");}

                |       exp_ar sgn_cmp exp_ar        {
                                                         Row row = (Row)$2.obj;
                                                         thirdGenerator.generarTerceto(row.getLexeme(),true);
                                                         thirdGenerator.showTercetos();
                                                     }

                |       exp_ar error exp_ar
                ;

bloque          :       '{' sentencias '}'
                |           sentencia
                |        '{'declaraciones sentencias'}'      { syntaxError("No se permiten declaraciones dentro de un bloque de sentencias.");}
                ;



do              :       T_RW_DO                                 {         

                                                                  Operand op = new Operand( thirdGenerator.countTercetos() );
                                                                  thirdGenerator.apilarOperando( op );

                                                                }
                               bloque
                                      while

                |       T_RW_DO error  while      { syntaxError("Sentencia DO WHILE:Bloque de sentencias erroneo.");}
                |       T_RW_DO while             { syntaxError("Sentencia DO WHILE:Se espera bloque de sentencias.");}
                ;


while           :      T_RW_WHILE condicion       {
                                                        thirdGenerator.generarTerceto("BF",true);

                                                        thirdGenerator.completarSalto(true);


                                                        thirdGenerator.generarTerceto("BI",false);

                                                        thirdGenerator.completarSaltoDo(true);

                                                        }


                |      T_RW_WHILE error         { syntaxError("Sentencia DO WHILE: Se espera condición. ");}
                ;

lado_izquierdo  :      variable

                ;


sgn_cmp         :       T_GT
                |       T_EQ
                |       T_LT
                |       T_BEQ
                |       T_GEQ
                |       T_ASSIGN { syntaxError("Se esperaba = en lugar de := "); }
                |       T_DISTINCT
                ;


exp_ar          :       exp_ar '+' term  {  thirdGenerator.generarTerceto("+",true); }

                |       exp_ar '-' term  {  thirdGenerator.generarTerceto("-",true); }

                |       term             { $$=$1;}

                |       exp_ar '+' error { syntaxError("Expresión aritmética: Operando no válido."); }
                |       exp_ar '-' error { syntaxError("Expresión aritmética: Operando no válido."); }
                ;


term            :       term '*' factor {  thirdGenerator.generarTerceto("*",true); }
                |       term '/' factor {  thirdGenerator.generarTerceto("/",true); }
                |       factor
                |       term '*' error { syntaxError("Expresión aritmética: Operando no válido."); }
                |       term '/' error { syntaxError("Expresión aritmética: Operando no válido."); }
                ;



factor          :       variable
                |       constante
                |       conversion '(' exp_ar ')'                 { 
                                                                      Row row = (Row)($1.obj);
                                                                      String operadorTerceto =row.getToken();
                                                                      thirdGenerator.generarTerceto(operadorTerceto,true);
                                                                  }

                | '('exp_ar')'                                 {
                                                                  syntaxError("No se permiten expresiones aritm�ticas entre par�ntesis.");
                                                               }

                ;


variable        :       T_IDENTIFIER                          { 

                                                                Row row = verifyDeclaration (decorator,($1.obj));
                                                                if ( row != null ){
                                                                    Operand op = new Operand(row.getLexeme(),row,symbolTable  );
                                                                    thirdGenerator.apilarOperando( op );
                                                                }
                                                              }
                
                |       T_IDENTIFIER T_PLUS_PLUS           // THIS IS THE NEW STUFF

                ;



constante       :       T_CONST                     { long val = 0;
                                                      Row row= (Row)$1.obj;
                                                      try {
                                                            val = Long.parseLong(row.getLexeme());
                                                            if ( (val+1) > 2147483647 )
                                                                 syntaxError(" Constante long " + row.getLexeme() + "  fuera de rango.");
                                                              else {
                                                                      Operand op = new Operand(row.getLexeme(),row,symbolTable);
                                                                      thirdGenerator.apilarOperando(op);
                                                                      thirdGenerator.showPila(thirdGenerator.getOperadores());
                                                                     }
                                                          
                                                        }
                                                        catch(NumberFormatException e){
                                                          e.printStackTrace();
                                                          } 
                                                      }

                |       '-' T_CONST                 {
                                                         long val = 0;
                                                         Row row= (Row)$2.obj;
                                                         try {
                                                              val = Long.parseLong(row.getLexeme());
                                                              if ( val > 2147483648 )
                                                                  syntaxError(" Constante long    -" + row.getLexeme() +  "  fuera de rango.");
                                                              else    {
                                                                        String lexCTE="-"+ row.getLexeme();
                                                                        row.setLexeme(lexCTE);
                                                                        row.setType("long");
                                                                        Row reference=symbolTable.getRow(lexCTE);
                                                                        if ( reference != null ){
                                                                               Operand op = new Operand(reference.getLexeme(),reference,symbolTable);
                                                                               thirdGenerator.apilarOperando(op);
                                                                               thirdGenerator.showPila(thirdGenerator.getOperadores());

                                                                          }
                                                                    }
                                                                }
                                                                catch(NumberFormatException e){
                                                                    e.printStackTrace();
                                                              }

                                                     }

                


                ;


tipo            :       T_RW_UINT             { tipo_identificador = "uint"; }

                |       T_RW_LONG             { tipo_identificador = "long"; }
                ;

conversion      :       T_RW_TOLONG
                ;


                             



impresion       :       T_RW_PRINT '(' T_STRING ')' { 
                                                      Row row1 =((Row)$1.obj);
                                                      Row row2 =((Row)$2.obj);
                                                      Row row3 =((Row)$3.obj);
                                                      Row row4 =((Row)$4.obj);
                                                      System.out.println( " REGLA  : T_RW_PRINT '(' T_STRING ')'  \n\n" + row1   +   " "  +  row2  +  "  "  +  row3  + "   "  + row4 ); 
                                                      Operand op = new Operand( row3.getLexeme(), row3, symbolTable );
                                                      thirdGenerator.apilarOperando( op );
                                                      thirdGenerator.generarTerceto( row1.getLexeme() ,false);

                                                    }

                |       T_RW_PRINT '['T_STRING']'   { syntaxError("Sentencia PRINT:Se espera ( y ) en lugar de [ ]."); }
                |       T_RW_PRINT '{'T_STRING'}'   { syntaxError("Sentencia PRINT:Se espera ( y ) en lugar de { }."); }
                |       T_RW_PRINT '('T_STRING error{ syntaxError("Sentencia PRINT:Se espera )");}
                |       T_RW_PRINT error            { syntaxError("Se espera ( luego de PRINT."); }
                ;


/*************************************************************************************************************************/
%%




int aux;
int nest;
int noEnvironment;
int maxNest;
NameDecorator decorator;
String tipo_identificador;
ThirdGenerator thirdGenerator;

String token;
String stringEmpty;


Lexer lex ;
SymbolTable symbolTable;
String type; //Used in declarations

public void load(Lexer lex,SymbolTable st,ThirdGenerator thirdGenerator,NameDecorator decorator){

	this.symbolTable= st ;
	this.lex = lex ;
	this.thirdGenerator=thirdGenerator;
	this.decorator=decorator;
  nest = 0;
  maxNest= 10;
  aux=0;
	
}

public int yyerror(String s){

  Lexer.showMessage(s);
	return 0;

}

public int yylex(){

  /*
  boolean eof = false;
  eof = lex.endOfFile();
  if (eof){
  return  0;//(Short)codes.get("ENDOFFILE");
  }
  
  Row tok = (Row) lex.getToken();
  if(tok == null){
      Short x = (Short) codes.get("ENDOFFILE");
      return x;
   }
  
  //symbolTable.add(tok);
  

  yylval = new ParserVal(tok);
  Short s = (Short) codes.get(tok.getToken());
  //System.out.println(symbolTable);
  //System.out.println("[ PARSER RECOGNIZES ] " + tok);
  //System.out.println(" PARSER - TOKEN : " + tok);
  return s.intValue();
  */
  
  boolean eof=false;
  eof=lex.endOfFile();
  Row row= (Row)lex.getToken();
  
  Short code=0;
  
  if (row==null){
  
    return (Short)codes.get("ENDOFFILE");
  
  }

  if (eof){

      code=(Short)codes.get("ENDOFFILE");
  }

  else
      {
        yylval=new ParserVal(row);
        code= codes.get(row.getToken());
      }

    System.out.println("PARSER LEXER  : "  + row  );
    System.out.println("PARSER PARSER :"   + row  +  " CODE : "  +  codes.get(row.getToken()) );
    return code.intValue();
} 


  
static Hashtable<String, Short> codes;
static {

  codes = new Hashtable<String,Short>();
  
	  codes.put("*",new Short((short)'*'));
	  codes.put("+",new Short((short)'+'));
	  codes.put("-",new Short((short)'-'));
	  codes.put("/",new Short((short)'/'));
	  codes.put("<",T_LT);
	  codes.put("=",T_EQ);
	  codes.put(">",T_GT);
	  codes.put("<>",T_DISTINCT);
	  codes.put("<=",T_BEQ);
	  codes.put(">=",T_GEQ);
	  codes.put(":=",T_ASSIGN);
	  codes.put("++",T_PLUS_PLUS);
	  codes.put("(",new Short((short)'('));
	  codes.put(")",new Short((short)')'));
	  codes.put(":",new Short((short)':'));
	  codes.put(";",new Short((short)';'));
	  codes.put("{",new Short((short)'{'));
	  codes.put("}",new Short((short)'}'));
	  codes.put(",",new Short((short)','));
	  codes.put("tolong",T_RW_TOLONG);
	  codes.put("identifier",T_IDENTIFIER);
	  codes.put("const",T_CONST);
	  codes.put("if",T_RW_IF);
	  codes.put("else",T_RW_ELSE);
	  codes.put("do",T_RW_DO);
	  codes.put("while",T_RW_WHILE);
	  codes.put("uint",T_RW_UINT);
	  codes.put("long",T_RW_LONG);
	  codes.put("print",T_RW_PRINT);
	  codes.put("string",T_STRING);
    codes.put("ENDOFFILE",T_ENDOFFILE);

}
/*******************************************************************************************************************************

                        PROCEDIMIENTOS COMPLEMENTARIOS PARA LA GENERACION DE CODIGO

El conocimiento del tipo de una variable se realiza en la etapa de generacion de codigo intermedio,notese
que las variables ya se encuentran en la tabla de simbolos desde el proceso de tokenizacion.

*******************************************************************************************************************************/
//void yyerror(String msj) {}

void syntaxError(String mensaje) {
	
	Lexer.showError(mensaje);

}

void informarSentencia(String mensaje) {
  Lexer.showMessage(mensaje);
}



void updateDeclaration ( Row symbolTableRow, String identifierType) {
        if ( ( symbolTableRow != null ) && ( !symbolTableRow.isEmptyType() ) ){
                Lexer.showMessage("  El identificador  " +   symbolTableRow.getLexeme()+ "  ya ha sido declarado.");
        }
        else
        if ( symbolTableRow != null )
                symbolTableRow.setType(identifierType);


}


/*******************************************************************************/

Row verifyDeclaration (NameDecorator decorer, Object obj ) {

        int zero = 0;
        Row ptr=null;
        String newName;
        String lexeme;
        Row ptrSymTable = (Row)obj;
           if ( (decorator != null ) && ( ptrSymTable != null ) ){
                lexeme =  decorer.getDecorado();
                //newName= lexeme + "_" +  (IntToStr(decorer.nroAmbito)).c_str() + "_" + (IntToStr(decorer.nroAnidamiento)).c_str();
                newName= lexeme + "_" +  Integer.toString(decorer.nroAmbito) + "_" + Integer.toString(decorer.nroAnidamiento);
                ////System.out.println(" NewName : "  + newName);
				        ptr=symbolTable.getRow(newName);
                if ( ( ptr !=null ) && (ptr.isEmptyType() ) ){
                        //newName=lexeme + "_" +  (IntToStr(zero)).c_str() + "_" + (IntToStr(zero)).c_str();
                        newName=lexeme + "_" + Integer.toString(zero) + "_" + Integer.toString(zero);
                        ptr=symbolTable.getRow(newName);
                        if ( ( ptr !=null ) && ( ptr.isEmptyType()) ) {
                               syntaxError(" El identificador : " + lexeme + " no ha sido declarado.");
                               }
                        else   if ( ptr==null)  //
                                syntaxError(" El identificador : " + lexeme + " no ha sido declarado.");//
                       }
                }

       return ptr;
}




