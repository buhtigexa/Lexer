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
        | '{' declaraciones  conjunto_sentencias'}' T_ENDOFFILE { System.out.println(" Programa aceptado con TS de :" + symbolTable.size()  + " -- Contenido : \n "   /* + symbolTable*/ );}
        | '{'declaraciones'}' T_ENDOFFILE
                        //{syntaxError("Se esperan sentencias ejecutables a continuaci�n de las declaraciones.");}
        | '{' /*{ syntaxError("Se esperan declaraciones al inicio del programa.");}*/ conjunto_sentencias '}' 
        | declaracion_sentencia T_ENDOFFILE
        | declaraciones conjunto_sentencias T_ENDOFFILE   
        ;


declaracion_sentencia :declaracion 
                      | sentencia  
                      ;

declaraciones: declaracion
              |declaraciones declaracion
              ;

declaracion: d_variable  { informarSentencia("Declaraci�n de identificadores");}
           ;


d_variable : tipo lista_identificadores ';'
           | tipo lista_identificadores error { syntaxError("Se espera ; "); }
           | tipo error  { syntaxError("Faltan los identificadores a declarar."); }
           ;



lista_identificadores   : T_IDENTIFIER  {  updateDeclaration((Row)$1.obj,tipo_identificador);    }

                        | lista_identificadores ',' T_IDENTIFIER   {  updateDeclaration((Row)$3.obj,tipo_identificador);    }

                        | lista_identificadores T_IDENTIFIER       { syntaxError("Se espera , entre identificadores"); }
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


sentencia       :         asignacion                    {       syntaxError("Se espera ; al finalizar la sentencia.")  ;}

                |         impresion                     {       syntaxError("Se espera ; al finalizar la sentencia.")  ;}

                |         do                            {       syntaxError("Se espera ; al finalizar la sentencia.") ;}


                |        { informarSentencia("Sentencia IF."); } if

                |          asignacion';'

                |         do';'

                |         impresion';'
                ;


asignacion      :        lado_izquierdo T_ASSIGN exp_ar         {        codeGenerator.generarTerceto(":=",false);


                                                               }


                |        lado_izquierdo T_EQ exp_ar             {  syntaxError("Asignacion: Se espera := en lugar de =");}
                |        lado_izquierdo T_ASSIGN error
                |        lado_izquierdo T_EQ error              { syntaxError("Asignacion:  Operador y expresiones de asignacion err�neas."); }
                |        lado_izquierdo error                   { syntaxError("Asignacion:  expresi�n no v�lida.");}
                ;

if              :       T_RW_IF condicion            {  codeGenerator.generarTerceto("BF",true);
                                                     }

                                         bloque       { codeGenerator.completarSalto(true); }
                                                else


                |       T_RW_IF condicion error else       {     syntaxError("Sentencia IF:Se espera { en el bloque de sentencias.");}
                |       T_RW_IF error bloque    else       { 	 syntaxError("Sentencia IF:Se espera ( ) ");}
                |       T_RW_IF error else
                ;



else            :       T_RW_ELSE                       {  codeGenerator.generarTerceto("BI",true);
                                                        }

                                   bloque               {
                                                          codeGenerator.completarSalto(false);
                                                          codeGenerator.showPila(codeGenerator.getOperadores());
                                                        }

                |       T_RW_ELSE error                 { syntaxError("Sentencia IF-ELSE:Se espera sentencia o bloque de sentencias."); }

                |       error                           { syntaxError("Sentencia IF-ELSE:Se espera ELSE o el bloque de sentencias es err�neo.");}
                ;



condicion       :       '('comparacion')'            {
                                                       codeGenerator.showPila(codeGenerator.getOperadores());
                                                       codeGenerator.desapilarOperando();
                                                     }

                |        '(' error ')'               {syntaxError("Se esperaba ( ) en lugar de { }");}
                ;


comparacion     :       comparacion sgn_cmp exp_ar   {  codeGenerator.generarTerceto( ((Row)($2.obj)).getLexeme(),true);
                                                        //codeGenerator.showTercetos();
                                                      }


                |       comparacion error  exp_ar    { syntaxError("Operador de comparaci�n no v�lido.");}

                |       exp_ar sgn_cmp exp_ar        {
                                                       codeGenerator.generarTerceto(((Row)($2.obj)).getLexeme(),true);
                                                        // codeGenerator.showTercetos();
                                                     }

                |       exp_ar error exp_ar
                ;


bloque          :       '{' sentencias '}'
                |           sentencia
                |        '{'declaraciones sentencias'}'      { syntaxError("No se permiten declaraciones dentro de un bloque de sentencias.");}

      // mod.1. |       '{' error '}'                        { syntaxError("No se permiten declaraciones dentro de un bloque de sentencias.");}
                ;



do              :       T_RW_DO                                 {         Operand op = new Operand( codeGenerator.countTercetos() );
                                                                          codeGenerator.apilarOperando( op );

                                                                }
                               bloque
                                      while

                |       T_RW_DO error  while      {syntaxError("Sentencia DO WHILE:Bloque de sentencias erroneo.");}
                |       T_RW_DO while             {syntaxError("Sentencia DO WHILE:Se espera bloque de sentencias.");}
                ;


while           :      T_RW_WHILE condicion       {
                                                        codeGenerator.generarTerceto("BF",true);

                                                        codeGenerator.completarSalto(true);


                                                        codeGenerator.generarTerceto("BI",false);

                                                        codeGenerator.completarSaltoDo(true);

                                                        }


                |      T_RW_WHILE error         { syntaxError("Sentencia DO WHILE: Se espera condici�n. ");}
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


exp_ar          :       exp_ar '+' term  {  codeGenerator.generarTerceto("+",true); }

                |       exp_ar '-' term  {  codeGenerator.generarTerceto("-",true); }

                |       term             { $$=$1;}

                |       exp_ar '+' error { syntaxError("Expresi�n aritm�tica: Operando no v�lido."); }
                |       exp_ar '-' error { syntaxError("Expresi�n aritm�tica: Operando no v�lido."); }
                ;


term            :       term '*' factor {  codeGenerator.generarTerceto("*",true); }
                |       term '/' factor {  codeGenerator.generarTerceto("/",true); }
                |       factor
                |       term '*' error { syntaxError("Expresi�n aritm�tica: Operando no v�lido."); }
                |       term '/' error { syntaxError("Expresi�n aritm�tica: Operando no v�lido."); }
                ;


factor          :        variable
                |       constante
                |       conversion '(' exp_ar ')'                   {
                                                                      String operadorTerceto =((Row)$1.obj).getLexeme();
                                                                      //System.out.println(" Operador TOLONG : "  + operadorTerceto);
                                                                      codeGenerator.generarTerceto(operadorTerceto,true);
                                                                  }

                | '('exp_ar')'                                 {
                                                                  syntaxError("No se permiten expresiones aritm�ticas entre par�ntesis.");
                                                               }

                ;


variable        :       T_IDENTIFIER                {      //Row *refDecl = verifyDeclaration (decorator,($1.obj));
                                                        Row refDecl = verifyDeclaration(decorator,(Row)($1.obj));
                                                        if ( refDecl != null ){
                                                        	Operand op = new Operand((refDecl).getLexeme(),refDecl,symbolTable  );
                                                            //System.out.println(" Apilando operando : " + (refDecl).getLexeme());
                                                            codeGenerator.apilarOperando( op );
                                                        }
                                                    }
                
                |       T_IDENTIFIER T_PLUS_PLUS           // THIS IS THE NEW STUFF

                ;



constante       :       T_CONST                    	{          //double val =StrToFloat (      ( ($1.obj).getLexeme() ).c_str() );
                                                               double val = new Double(0.0);
                                                               try{
                                                               		val=Double.parseDouble( ((Row)($1.obj)).getLexeme() );
                                                               		  if ( val > 2147483647.0 )
                                                                               syntaxError(" Constante long " + ((Row)($1.obj)).getLexeme() + "  fuera de rango.");
                                                               				else {
                                                                            	    Operand op = new Operand(((Row)($1.obj)).getLexeme(),($1.obj),symbolTable);
                                                                                	codeGenerator.apilarOperando(op);
                                                                                	codeGenerator.showPila(codeGenerator.getOperadores());
                                                               					}
														}
															catch(NumberFormatException e){
																	System.out.println(" Error en conversión del flotante " + ((Row)($1.obj)).getLexeme());
                                                               		e.printStackTrace();
                                                               	}
                                                        }

                |       '-' T_CONST                 {
                                                         //double val =StrToFloat(  (($2.obj).getLexeme()).c_str());
                                                         try{ 
                                                         	double val = Double.parseDouble(((Row)($2.obj)).getLexeme());
	                                                         if ( val > 2147483648.0 )
    	                                                            syntaxError(" Constante long    -" + (((Row)($2.obj)).getLexeme()) +  "  fuera de rango.");

                                                        		else    {     String lexCTE="-"+ ((Row)($2.obj)).getLexeme();
                                                                	      ((Row)($2.obj)).setLexeme(lexCTE);
                                                                    	  ((Row)($2.obj)).setType("long");
                                                                      	Row f=symbolTable.getRow(lexCTE);
                                                                      	if ( f != null ){
                                                                               Operand op = new Operand(((Row)($2.obj)).getLexeme(),($2.obj),symbolTable);
                                                                               codeGenerator.apilarOperando(op);
                                                                               codeGenerator.showPila(codeGenerator.getOperadores());

                                                                      }
                                                                      yylval.obj=f;
                                                                }
                                                            }
                                                                catch(NumberFormatException e){

                                                                	System.out.println(" Error en conversión del flotante " + ((Row)($1.obj)).getLexeme());
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
                                                        informarSentencia("Sentencia PRINT.");
                                                        Operand op = new Operand( ((Row)($3.obj)).getLexeme(),($3.obj),symbolTable );
                                                        codeGenerator.apilarOperando( op );
                                                        codeGenerator.generarTerceto(((Row)($1.obj)).getToken(),false);
                                                        
                                                        //System.out.println(" Operandos : " +  ((Row)$1.obj).getLexeme()  + "  "   + ((Row)$3.obj).getLexeme()  );

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
ThirdGenerator codeGenerator;
Console console;
String token;
String stringEmpty;


Lexer lex ;
SymbolTable symbolTable;
String type; //Used in declarations

public void load(Lexer lex,SymbolTable st,ThirdGenerator codeGenerator,NameDecorator decorator,Console console){

	this.symbolTable= st ;
	this.lex = lex ;
	this.codeGenerator=codeGenerator;
	this.decorator=decorator;
	this.console=console;
}

public int yyerror(String s){

  System.out.println(s);
	return 0;

}

public int yylex(){

  
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
	//TForm1::writer("[Error] Línea " + Lexer->getNroLinea() + ": " + mensaje + ".");
    //TForm1::incErrors();
	console.show(mensaje);
}

void informarSentencia(String mensaje) {
	//TForm1::writer("Línea " + Lexer->getNroLinea() + ": " + mensaje + ".");
	System.out.println("Línea " + lex.getLine() + ": " + mensaje + ".");
}



void updateDeclaration ( Row symbolTableRow, String identifierType) {
        if ( ( symbolTableRow != null ) && ( !symbolTableRow.isEmptyType() ) ){
                //TForm1::writer("L�nea " + Lexer.getLine() + ":  El identificador  " +   symbolTableRow.getLexeme()+ "  ya ha sido declarado.");
        		System.out.println("L�nea " + lex.getLine() + ":  El identificador  " +   symbolTableRow.getLexeme()+ "  ya ha sido declarado.");
        }
        else
        if ( symbolTableRow != null )
                symbolTableRow.setType(identifierType);


}


/*******************************************************************************/

Row verifyDeclaration (NameDecorator decorer, Row ptrSymTable ) {

        int zero = 0;
        Row ptr=null;
        String newName;
        String lexeme;
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




