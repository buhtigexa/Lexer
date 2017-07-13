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
        | '{' declaraciones  conjunto_sentencias'}' T_ENDOFFILE { System.out.println(" Programa aceptado ");}
        | '{'declaraciones'}' 
                        //{syntaxError("Se esperan sentencias ejecutables a continuaci�n de las declaraciones.");}
        | '{' /*{ syntaxError("Se esperan declaraciones al inicio del programa.");}*/ conjunto_sentencias '}' 
        | declaracion_sentencia 
        | declaraciones conjunto_sentencias   
        ;


declaracion_sentencia :declaracion 
                      | sentencia  
                      ;

declaraciones: declaracion
              |declaraciones declaracion
              ;

declaracion: d_variable  //{ informarSentencia("Declaraci�n de identificadores");}
           ;


d_variable : tipo lista_identificadores ';'
           | tipo lista_identificadores error //{ syntaxError("Se espera ; "); }
           | tipo error  //{ syntaxError("Faltan los identificadores a declarar."); }
           ;



lista_identificadores   : T_IDENTIFIER  //{  updateDeclaration($1.ptr,tipo_identificador);    }

                        | lista_identificadores ',' T_IDENTIFIER   //{  updateDeclaration($3.ptr,tipo_identificador);    }

                        | lista_identificadores T_IDENTIFIER        //{ syntaxError("Se espera , entre identificadores"); }
                        ;


conjunto_sentencias : sentencia
                    | conjunto_sentencias   /*{
                                              decorator->nroAnidamiento = decorator->nroAnidamiento + 1;
                                              if ( decorator->nroAnidamiento == 1 ) {
                                                   decorator->nroAmbito  = aux;
                                                   decorator->nroAmbito++;

                                                   }
                                             }*/ '{' ambito '}' /*{ if ( decorator->nroAnidamiento >= 2  )
                                                                     syntaxError("No se permite anidamiento de ambitos.");
                                                                     decorator->nroAnidamiento=decorator->nroAnidamiento - 1;
                                                                     if ( decorator->nroAnidamiento < 1 ) {
                                                                                    decorator->nroAnidamiento= 0;
                                                                                    aux = decorator->nroAmbito;
                                                                                    decorator->nroAmbito = 0;
                                                                                  }
                                                                     }*/

                    |  /* { syntaxError("Se esperan sentencias ejecutables antes de la declaraci�n de un �mbito.");
                          decorator->nroAnidamiento = decorator->nroAnidamiento + 1;
                                  if ( decorator->nroAnidamiento == 1 ){
                                                                   decorator->nroAmbito  = aux;
                                                                   decorator->nroAmbito ++;

                                                                }
                         }*/ '{' ambito '}' /*{ if ( decorator->nroAnidamiento >= 2  )
                                                            syntaxError("No se permite anidamiento de ambitos.");
                                                            decorator->nroAnidamiento=decorator->nroAnidamiento - 1;
                                                            if ( decorator->nroAnidamiento < 1 ){
                                                                                  decorator->nroAnidamiento = 0;
                                                                                  aux = decorator->nroAmbito;
                                                                                  decorator->nroAmbito = 0;
                                                              }


                                            }*/
                    | conjunto_sentencias sentencia
                    | conjunto_sentencias declaracion //{ syntaxError("Las declaraciones deben preceder a las sentencias ejecutables."); }
                    ;


sentencias : sentencia
           | sentencias sentencia
           ;


ambito  :   /*{ informarSentencia("Declaraci�n de ambito.");}*/  declaraciones conjunto_sentencias

        |  /*{ informarSentencia("Declaraci�n de ambito.");}*/ conjunto_sentencias

        |   error   /*{ syntaxError("Declarci�n de ambito erroneo");}*/
        ;


sentencia       :         asignacion                   /* {       syntaxError("Se espera ; al finalizar la sentencia.")  ;}*/

                |         impresion                    /* {       syntaxError("Se espera ; al finalizar la sentencia.")  ;}*/

                |         do                           /* {       syntaxError("Se espera ; al finalizar la sentencia.") ;}*/


                |        /*{ informarSentencia("Sentencia IF."); }*/ if

                |          asignacion';'

                |         do';'

                |         impresion';'
                ;


asignacion      :        lado_izquierdo T_ASSIGN exp_ar        /* {        codeGenerator->generarTerceto(":=",false);


                                                               }*/


                |        lado_izquierdo T_EQ exp_ar             /*{  syntaxError("Asignacion: Se espera := en lugar de =");}*/
                |        lado_izquierdo T_ASSIGN error
                |        lado_izquierdo T_EQ error              /*{ syntaxError("Asignacion:  Operador y expresiones de asignacion err�neas."); }*/
                |        lado_izquierdo error                   /*{ syntaxError("Asignacion:  expresi�n no v�lida.");}*/
                ;

if              :       T_RW_IF condicion            /*{  codeGenerator->generarTerceto("BF",true);
                                                     }*/

                                         bloque       /*{ codeGenerator->completarSalto(true); }*/
                                                else


                |       T_RW_IF condicion error else      // {     syntaxError("Sentencia IF:Se espera { en el bloque de sentencias.");}
                |       T_RW_IF error bloque    else      // { syntaxError("Sentencia IF:Se espera ( ) ");}
                |       T_RW_IF error else
                ;



else            :       T_RW_ELSE                       //{  codeGenerator->generarTerceto("BI",true);
                                                        // }

                                   bloque               /*{
                                                          codeGenerator->completarSalto(false);
                                                          codeGenerator->showPila(codeGenerator->getOperadores());
                                                        }*/

                |       T_RW_ELSE error                 //{ syntaxError("Sentencia IF-ELSE:Se espera sentencia o bloque de sentencias."); }

                |       error                           //{ syntaxError("Sentencia IF-ELSE:Se espera ELSE o el bloque de sentencias es err�neo.");}
                ;



condicion       :       '('comparacion')'            /*{
                                                       codeGenerator->showPila(codeGenerator->getOperadores());
                                                       codeGenerator->desapilarOperando();
                                                     }*/

                |        '(' error ')'               /*{syntaxError("Se esperaba ( ) en lugar de { }");}*/
                ;


comparacion     :       comparacion sgn_cmp exp_ar   /*{  codeGenerator->generarTerceto(*($2.op),true);
                                                          codeGenerator->showTercetos();
                                                       }*/


                |       comparacion error  exp_ar    /*{ syntaxError("Operador de comparaci�n no v�lido.");}*/

                |       exp_ar sgn_cmp exp_ar        /*{
                                                       codeGenerator->generarTerceto(*($2.op),true);
                                                         codeGenerator->showTercetos();
                                                     }*/

                |       exp_ar error exp_ar
                ;


bloque          :       '{' sentencias '}'
                |           sentencia
                |        '{'declaraciones sentencias'}'      //{ syntaxError("No se permiten declaraciones dentro de un bloque de sentencias.");}

      // mod.1. |       '{' error '}'                        { syntaxError("No se permiten declaraciones dentro de un bloque de sentencias.");}
                ;



do              :       T_RW_DO                                 /*{         Operando *op = new Operando( codeGenerator->countTercetos() );
                                                                          codeGenerator->apilarOperando( op );

                                                                }*/
                               bloque
                                      while

                |       T_RW_DO error  while     // {syntaxError("Sentencia DO WHILE:Bloque de sentencias erroneo.");}
                |       T_RW_DO while            // {syntaxError("Sentencia DO WHILE:Se espera bloque de sentencias.");}
                ;


while           :      T_RW_WHILE condicion       /*{
                                                        codeGenerator->generarTerceto("BF",true);

                                                        codeGenerator->completarSalto(true);


                                                        codeGenerator->generarTerceto("BI",false);

                                                        codeGenerator->completarSaltoDo(true);

                                                        }*/


                |      T_RW_WHILE error         //{ syntaxError("Sentencia DO WHILE: Se espera condici�n. ");}
                ;


lado_izquierdo  :      variable

                ;


sgn_cmp         :       T_GT
                |       T_EQ
                |       T_LT
                |       T_BEQ
                |       T_GEQ
                |       T_ASSIGN //{ syntaxError("Se esperaba = en lugar de := "); }
                |       T_DISTINCT
                ;


exp_ar          :       exp_ar '+' term  //{  codeGenerator->generarTerceto("+",true); }

                |       exp_ar '-' term  //{  codeGenerator->generarTerceto("-",true); }

                |       term             { $$=$1;}

                |       exp_ar '+' error //{ syntaxError("Expresi�n aritm�tica: Operando no v�lido."); }
                |       exp_ar '-' error //{ syntaxError("Expresi�n aritm�tica: Operando no v�lido."); }
                ;


term            :       term '*' factor //{  codeGenerator->generarTerceto("*",true); }
                |       term '/' factor //{  codeGenerator->generarTerceto("/",true); }
                |       factor
                |       term '*' error //{ syntaxError("Expresi�n aritm�tica: Operando no v�lido."); }
                |       term '/' error //{ syntaxError("Expresi�n aritm�tica: Operando no v�lido."); }
                ;


factor          :        variable
                |       constante
                |       conversion '(' exp_ar ')'                   /*{
                                                                      string operadorTerceto =(*($1.op));
                                                                      codeGenerator->generarTerceto(operadorTerceto,true);
                                                                  }*/

                | '('exp_ar')'                                /* {
                                                                  syntaxError("No se permiten expresiones aritm�ticas entre par�ntesis.");
                                                               }*/

                ;


variable        :       T_IDENTIFIER                /*{      Fila *refDecl = verifyDeclaration (decorator,($1.ptr));
                                                           if ( refDecl != NULL ){
                                                                        Operando *op = new Operando((refDecl)->getLexeme(),refDecl,symbolTable  );
                                                                codeGenerator->apilarOperando( op );
                                                                }
                                                    }*/
                
                |       T_IDENTIFIER T_PLUS_PLUS           // THIS IS THE NEW STUFF

                ;



constante       :       T_CONST                     /*{          double val =StrToFloat (      ( ($1.ptr)->getLexeme() ).c_str() );
                                                               if ( val > 2147483647 )
                                                                               syntaxError(" Constante long " + ($1.ptr)->getLexeme() + "  fuera de rango.");
                                                               else {
                                                                                Operando *op = new Operando(($1.ptr)->getLexeme(),($1.ptr),symbolTable);
                                                                                codeGenerator->apilarOperando(op);
                                                                                codeGenerator->showPila(codeGenerator->getOperadores());
                                                               }
                                                    }*/

                |       '-' T_CONST                 /*{
                                                         double val =StrToFloat(  (($2.ptr)->getLexeme()).c_str());
                                                         if ( val > 2147483648 )
                                                                syntaxError(" Constante long    -" + (($2.ptr)->getLexeme()) +  "  fuera de rango.");

                                                        else    {     string lexCTE="-"+ ($2.ptr)->getLexeme();
                                                                      ($2.ptr)->setLexeme(lexCTE);
                                                                      ($2.ptr)->setType("long");
                                                                      Fila *f=symbolTable->getFila(lexCTE);
                                                                      if ( f != NULL ){
                                                                               Operando *op = new Operando(($2.ptr)->getLexeme(),($2.ptr),symbolTable);
                                                                               codeGenerator->apilarOperando(op);
                                                                               codeGenerator->showPila(codeGenerator->getOperadores());

                                                                      }
                                                                      yylval.ptr=f;

                                                                }
                                                     }*/



                ;


tipo            :       T_RW_UINT             //{ tipo_identificador = "uint"; }

                |       T_RW_LONG             //{ tipo_identificador = "long"; }
                ;

conversion      :       T_RW_TOLONG
                ;



impresion       :       T_RW_PRINT '(' T_STRING ')' /*{
                                                        informarSentencia("Sentencia PRINT.");

                                                        Operando *op = new Operando( ($3.ptr)->getLexeme(),($3.ptr),symbolTable );
                                                        codeGenerator->apilarOperando( op );
                                                        codeGenerator->generarTerceto((*$1.op),false);

                                                  }*/

                |       T_RW_PRINT '['T_STRING']'   //{ syntaxError("Sentencia PRINT:Se espera ( y ) en lugar de [ ]."); }
                |       T_RW_PRINT '{'T_STRING'}'   //{ syntaxError("Sentencia PRINT:Se espera ( y ) en lugar de { }."); }
                |       T_RW_PRINT '('T_STRING error//{ syntaxError("Sentencia PRINT:Se espera )");}
                |       T_RW_PRINT error            //{ syntaxError("Se espera ( luego de PRINT."); }
                ;


/*************************************************************************************************************************/
%%



SymbolTable symbolTable;
int aux;
int nest;
int noEnvironment;
int maxNest;
//NameDecorator decorator;

//ThirdGenerator codeGenerator;

String token;
String stringEmpty;


Lexer lex ;
SymbolTable st ;
String type; //Used in declarations

public void load(Lexer lex,SymbolTable st ){

	this.st= st ;
	this.lex = lex ;
	

}

public int yyerror(String s){

  System.out.println(s);
	return 0;

}

public int yylex(){

  
  boolean eof = false;
  eof = lex.endOfFile();

	if (eof){
    return (Short)codes.get("ENDOFFILE");
  } 
	

  Row tok = (Row) lex.getToken();
	if(tok == null){
		  Short x = (Short) codes.get("ENDOFFILE");
      return x;
   }


	yylval = new ParserVal(tok);

	Short s = (Short) codes.get(tok.getToken());

  System.out.println("[ PARSER - TOKEN  ] " + tok);
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
/*
void updateDeclaration ( Fila *symbolTableRow, string identifierType) {
        if ( ( symbolTableRow != NULL ) && ( !symbolTableRow->isEmptyType() ) )
                TForm1::writer("L�nea " + Lexer->getNroLinea() + ":  El identificador  " +   symbolTableRow->getLexeme()+ "  ya ha sido declarado.");
        else
        if ( symbolTableRow != NULL )
                symbolTableRow->setType(identifierType);


}
*/

/*******************************************************************************/
/*
Fila * verifyDeclaration (nameDecorator *decorer, Fila *ptrSymTable ) {

        int zero = 0;
        Fila *ptr;
        ptr=NULL;
        string newName;
        string lexeme;
           if ( (decorator != NULL ) && ( ptrSymTable != NULL ) ){
                lexeme =  decorer->getDecorado();
                newName= lexeme + "_" +  (IntToStr(decorer->nroAmbito)).c_str() + "_" + (IntToStr(decorer->nroAnidamiento)).c_str();
                ptr=symbolTable->getFila(newName);
                if ( ( ptr !=NULL ) && (ptr->isEmptyType() ) ){
                        newName=lexeme + "_" +  (IntToStr(zero)).c_str() + "_" + (IntToStr(zero)).c_str();
                        ptr=symbolTable->getFila(newName);
                        if ( ( ptr !=NULL ) && ( ptr->isEmptyType()) ) {
                               syntaxError(" El identificador : " + lexeme + " no ha sido declarado.");
                               }
                        else   if ( ptr==NULL)  //
                                syntaxError(" El identificador : " + lexeme + " no ha sido declarado.");//
                       }
                }

       return ptr;
}
*/



