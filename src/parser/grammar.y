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
import generadorCodigo.*;
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


asignacion      :        lado_izquierdo T_ASSIGN exp_ar      {  
                                                                  Row token=(Row)$2.obj;
                                                                  Terceto terceto = new Terceto (token.getToken(),(Row)$1.obj,$3.obj);
                                                                  generadorTercetos.add(terceto);
                                                                  System.out.println(terceto);
                                                                  $$.obj=(Row)$1.obj;


                                                              }   


                |        lado_izquierdo T_EQ exp_ar             
                |        lado_izquierdo T_ASSIGN error
                |        lado_izquierdo T_EQ error              
                |        lado_izquierdo error                                   ;


if              :       T_RW_IF condicion           {  
                                                          // acá pasa la condición :
                                                          // se genera el BF y se apila .
                                                         
                                                          Terceto terceto=new Terceto("BF",new Completable(),new Completable());
                                                          generadorTercetos.apilar(terceto);
                                                          generadorTercetos.add(terceto);
                                                          //$$.obj=terceto;   
                                                          $$.obj=new Referencia(terceto.getId());
                                                    }


                                         bloque       { 
                                                          
                                                          Terceto BF=(Terceto)generadorTercetos.desapilar();
                                                          Terceto BI = new Terceto("BI",new Completable(),new Completable());
                                                          generadorTercetos.add(BI);
                                                          generadorTercetos.apilar(BI);
                                                          Referencia salto_false=new Referencia(generadorTercetos.getNextTercetoId());
                                                          generadorTercetos.completarTerceto(BF,salto_false);

                                                      }
                                                else


                |       T_RW_IF condicion error else  {     syntaxError("Sentencia IF:Se espera { en el bloque de sentencias.");}
                |       T_RW_IF error bloque    else       { syntaxError("Sentencia IF:Se espera ( ) ");}
                |       T_RW_IF error else
                ;



else            :       T_RW_ELSE                        { 

                                                         }

                                   bloque               { 
                                                          // acá se completa el statement
                                                          // se desapila y se completa el BI
                                                          Terceto BI=(Terceto)generadorTercetos.desapilar();
                                                          Referencia salto_true=new Referencia(generadorTercetos.getNextTercetoId());
                                                          generadorTercetos.completarTerceto(BI,salto_true);
                                                          
                                                        }

                |       T_RW_ELSE error                 { syntaxError("Sentencia IF-ELSE:Se espera sentencia o bloque de sentencias."); }

                |       error                           { syntaxError("Sentencia IF-ELSE:Se espera ELSE o el bloque de sentencias es erróneo.");}
                ;



condicion       :       '('comparacion')'            {
                                                      
                                                        //Terceto terceto=new Terceto();  
                                                        $$=$2;
                                                     
                                                     }

                |        '(' error ')'               {syntaxError("Se esperaba ( ) en lugar de { }");}
                ;


comparacion     :       comparacion sgn_cmp exp_ar   {  
                                                          Row operador = (Row)$2.obj;
                                                          Terceto terceto = new Terceto(operador.getToken(),$1.obj,$3.obj);
                                                          generadorTercetos.add(terceto);
                                                          System.out.println(terceto);
                                                          //$$.obj=terceto;
                                                          $$.obj=new Referencia(terceto.getId());
                                                       }


                |       comparacion error  exp_ar    { syntaxError("Operador de comparación no válido.");}

                |       exp_ar sgn_cmp exp_ar        {  
                                                          Row operador = (Row)$2.obj;
                                                          Terceto terceto = new Terceto(operador.getToken(),$1.obj,$3.obj);
                                                          generadorTercetos.add(terceto);
                                                          System.out.println(terceto);
                                                          //$$.obj=terceto;
                                                          $$.obj=new Referencia(terceto.getId());
                                                     }

                |       exp_ar error exp_ar
                ;

bloque          :       '{' sentencias '}'
                |           sentencia
                |        '{'declaraciones sentencias'}'      { syntaxError("No se permiten declaraciones dentro de un bloque de sentencias.");}
                ;



do              :       T_RW_DO                                 {
                                                                 
                                                                 Referencia inicio_loop = new Referencia(generadorTercetos.getNextTercetoId());
                                                                 generadorTercetos.apilar(inicio_loop);


                                                                }
                               bloque
                                      while

                |       T_RW_DO error  while      { syntaxError("Sentencia DO WHILE:Bloque de sentencias erroneo.");  }
                |       T_RW_DO while             { syntaxError("Sentencia DO WHILE:Se espera bloque de sentencias.");}
                ;


while           :      T_RW_WHILE condicion       { 

                                                    Terceto BF=new Terceto("BF",new Completable(),new Completable());
                                                    Terceto BI=new Terceto("BI",new Completable(),new Completable());
                                                    generadorTercetos.add(BF);
                                                    generadorTercetos.add(BI);
                                                    Referencia salto_false=new Referencia(generadorTercetos.getNextTercetoId());
                                                    Referencia inicio_loop=(Referencia)generadorTercetos.desapilar();
                                                    generadorTercetos.completarTerceto(BF,salto_false);
                                                    generadorTercetos.completarTerceto(BI,inicio_loop);

                                                  }


                |      T_RW_WHILE error         { syntaxError("Sentencia DO WHILE: Se espera condición. ");}
                ;

lado_izquierdo  :      variable                  {  
                                                     
                                                    $$=$1;
                                                }

                ;


sgn_cmp         :       T_GT
                |       T_EQ
                |       T_LT
                |       T_BEQ
                |       T_GEQ
                |       T_ASSIGN { syntaxError("Se esperaba = en lugar de := "); }
                |       T_DISTINCT
                ;


exp_ar          :       exp_ar '+' term           {  
                                                      
                                                      Terceto terceto = new Terceto ("+",$1.obj,$3.obj);
                                                      System.out.println(terceto);
                                                      generadorTercetos.add(terceto);
                                                      $$.obj=new Referencia(terceto.getId());
                                                  }


                |       exp_ar '-' term           {  
                                                      Terceto terceto = new Terceto ("-",$1.obj,$3.obj);
                                                      System.out.println(terceto);
                                                      generadorTercetos.add(terceto);
                                                      $$.obj=new Referencia(terceto.getId());

                                                    }

                |       term             {    
                                            $$=$1;   
                                         }

                |       exp_ar '+' error { syntaxError("Expresión aritmética: Operando no válido."); }
                |       exp_ar '-' error { syntaxError("Expresión aritmética: Operando no válido."); }
                ;


term            :       term '*' factor {  
                                          Terceto terceto = new Terceto ("*",$1.obj,$3.obj);
                                          System.out.println(terceto);
                                          generadorTercetos.add(terceto);
                                          $$.obj=new Referencia(terceto.getId());
                                        }

                |       term '/' factor { 
                                            Terceto terceto = new Terceto ("/",$1.obj,$3.obj);
                                            System.out.println(terceto);
                                            generadorTercetos.add(terceto);
                                            $$.obj=new Referencia(terceto.getId());
                                          }

                |       factor          { 
                                          $$=$1; 

                                        }
                |       term '*' error { syntaxError("Expresión aritmética: Operando no válido."); }
                |       term '/' error { syntaxError("Expresión aritmética: Operando no válido."); }
                ;




factor          :       variable                                 {  $$ = $1;   }
                |       constante                                {  $$ = $1;   }
                |       conversion '(' exp_ar ')'                 
                                                                  {  
                                                                    Row token=(Row)$1.obj;
                                                                    Terceto terceto = new Terceto (token.getToken(),new Completable(),$3.obj);
                                                                    System.out.println(terceto);
                                                                    generadorTercetos.add(terceto);
                                                                    //$$.obj=terceto;   
                                                                    $$.obj=new Referencia(terceto.getId());
                                                                  }

                | '('exp_ar')'                                 {
                                                                  syntaxError("No se permiten expresiones aritm�ticas entre par�ntesis.");
                                                               }

                ;


variable        :       T_IDENTIFIER                          {  
                                                               
                                                               Row row = verifyDeclaration (decorator,($1.obj));
                                                               $$.obj=row;
                                                               
                                                               }

                
                |       T_IDENTIFIER T_PLUS_PLUS           {  
                                                              Row id = (Row)$1.obj;
                                                              if (id.getType().compareTo("long")!=0){
                                                                Lexer.showError("La operación ++ , sólo está permitida para tipos long .");
                                                              }
                                                              Row constant = new RowConst("const","1","long");
                                                              Terceto terceto_plus = new Terceto ("+",$1.obj,constant);
                                                              generadorTercetos.add(terceto_plus);
                                                              Terceto terceto= new Terceto (":=",$1.obj,new Referencia(terceto_plus.getId()));
                                                              generadorTercetos.add(terceto);
                                                              System.out.println(terceto);
                                                              //$$.obj=terceto;
                                                              $$.obj=new Referencia(terceto.getId());

                                                            }
                ;



constante       :       T_CONST                     {  

                                                           
                                                      long val = 0;
                                                      Row row= (Row)$1.obj;
                                                      try {
                                                            val = Long.parseLong(row.getLexeme());
                                                            if ( val > 2147483647 )
                                                                 syntaxError(" Constante long " + row.getLexeme() + "  fuera de rango.");
                                                            

                                                            
                                                        }
                                                        catch(NumberFormatException e){
                                                          e.printStackTrace();
                                                        } 
                                                      
                                                      $$.obj=row;
                                                      System.out.println(" Constante  : "  +  $$.obj);


                                                      }


                |       '-' T_CONST                 {
                                                         
                                                         long val = 0;
                                                         Row row= (Row)$2.obj;
                                                         try {
                                                              val = Long.parseLong(row.getLexeme());
                                                              if ( (val-1) > 2147483647 )
                                                                  syntaxError(" Constante long    -" + row.getLexeme() +  "  fuera de rango.");
                                                              else 
                                                                    {
                                                                        String negativo="-"+ row.getLexeme();
                                                                        row.setLexeme(negativo);
                                                                        row.setType("long");
                                                                   }
                                                                }
                                                                catch(NumberFormatException e){
                                                                    e.printStackTrace();
                                                                }

                                                         $$.obj=row;       
                                                         System.out.println(" Constante negativa : "  +  $$.obj);

                                                     
                                                     }

                


                ;


tipo            :       T_RW_UINT             { tipo_identificador = "uint"; }

                |       T_RW_LONG             { tipo_identificador = "long"; }
                ;

conversion      :       T_RW_TOLONG           { $$ = $1; }                
                ;



                             



impresion       :       T_RW_PRINT '(' T_STRING ')' { 
                                                      
                                                      Row token  = (Row)$1.obj;
                                                      Row lexema = (Row)$3.obj;
                                                      Terceto terceto = new Terceto(token.getToken(),new Completable(),lexema);
                                                      generadorTercetos.add(terceto);
                                                      System.out.println(terceto);

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
GeneradorTercetos generadorTercetos;
int i;
String token;
String stringEmpty;


Lexer lex ;
SymbolTable symbolTable;
String type; //Used in declarations

public void load(Lexer lex,SymbolTable st,GeneradorTercetos generadorTercetos,NameDecorator decorator){

i=0;
	this.symbolTable= st ;
	this.lex = lex ;
	this.generadorTercetos=generadorTercetos;
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
  System.out.println(" PARSER - TOKEN : " + tok);
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

  //  System.out.println("PARSER LEXER  : "  + row  );
    //System.out.println("PARSER PARSER :"   + row  +  " CODE : "  +  codes.get(row.getToken()) );
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




