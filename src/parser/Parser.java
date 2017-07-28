//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 33 "grammar.y"


package parser;

import java.util.Hashtable;
import automaton.Lexer;
import symboltable.*;
import generadorCodigo.*;
import utils.*;


//#line 29 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short T_IDENTIFIER=257;
public final static short T_CONST=258;
public final static short T_UINT=259;
public final static short T_LONG=260;
public final static short T_STRING=261;
public final static short T_RW_IF=262;
public final static short T_RW_ELSE=263;
public final static short T_RW_DO=264;
public final static short T_RW_PRINT=265;
public final static short T_RW_WHILE=266;
public final static short T_RW_TOLONG=267;
public final static short T_RW_LONG=268;
public final static short T_RW_UINT=269;
public final static short T_GT=270;
public final static short T_EQ=271;
public final static short T_LT=272;
public final static short T_BEQ=273;
public final static short T_GEQ=274;
public final static short T_ASSIGN=275;
public final static short T_DISTINCT=276;
public final static short T_PLUS_PLUS=277;
public final static short T_ENDOFFILE=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    3,    3,    1,    1,
    4,    6,    6,    6,    8,    8,    8,    2,    9,    2,
   11,    2,    2,    2,   12,   12,   13,   10,   14,   10,
   10,   16,    5,    5,    5,    5,    5,    5,    5,   17,
   17,   17,   17,   17,   24,   26,   15,   15,   15,   15,
   27,   25,   25,   25,   22,   22,   28,   28,   28,   28,
   23,   23,   23,   30,   18,   18,   18,   31,   31,   20,
   29,   29,   29,   29,   29,   29,   29,   21,   21,   21,
   21,   21,   33,   33,   33,   33,   33,   34,   34,   34,
   34,   32,   32,   35,   35,    7,    7,   36,   19,   19,
   19,   19,   19,
};
final static short yylen[] = {                            2,
    0,    5,    4,    4,    2,    3,    1,    1,    1,    2,
    1,    3,    3,    2,    1,    3,    2,    1,    0,    5,
    0,    4,    2,    2,    1,    2,    0,    3,    0,    2,
    1,    0,    2,    2,    2,    2,    1,    1,    1,    3,
    3,    3,    3,    2,    0,    0,    6,    4,    4,    3,
    0,    3,    2,    1,    3,    3,    3,    3,    3,    3,
    3,    1,    4,    0,    4,    3,    2,    2,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    3,    3,    1,
    3,    3,    3,    3,    1,    3,    3,    1,    1,    4,
    3,    1,    2,    1,    2,    1,    1,    1,    4,    4,
    4,    4,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   97,   96,    0,    0,    0,    0,    0,
    8,   11,    0,    0,    0,    0,    0,    0,   70,   93,
    0,    0,    0,   67,  103,    0,    0,    0,    0,    0,
    9,   18,    0,    0,   10,    5,   14,   15,    0,    0,
   33,   34,   35,   36,   44,    0,    0,   66,   69,    0,
   68,    0,   62,    0,    0,    0,    0,    0,    0,    0,
   24,   23,    0,    0,    6,   13,   17,   12,    0,    0,
    0,   43,   94,   98,    0,    0,    0,   88,    0,   85,
   89,    0,   42,    0,    0,    0,    0,    0,   25,    0,
   65,  101,  102,   99,  100,    3,    0,    4,    0,   31,
    0,    0,    0,   16,   54,    0,    0,   50,    0,    0,
   95,    0,    0,    0,    0,    0,    0,   56,    0,   71,
   72,   73,   74,   75,   76,   77,    0,    0,   55,    0,
    0,   61,   26,    2,    0,   22,    0,    0,   53,    0,
   49,   48,   46,   91,   81,    0,   82,    0,   86,   83,
   87,   84,    0,    0,    0,    0,    0,   63,   20,    0,
   52,    0,   90,   47,
};
final static short yydgoto[] = {                          7,
    8,   30,    9,   61,   32,   12,   13,   39,   63,  101,
   33,   90,  102,  103,   41,   14,   15,   16,   17,   18,
   77,   51,   54,  110,  108,  162,  140,   87,  127,   23,
   24,   19,   79,   80,   81,   82,
};
final static short yysindex[] = {                       -75,
 -259, -240,  -35,    0,    0, -187,    0, -187, -241,    0,
    0,    0, -155, -220,   -1,   20,   26, -225,    0,    0,
 -177,  -28,  -69,    0,    0, -166, -148, -143,  144,  273,
    0,    0,  -20, -142,    0,    0,    0,    0,  -14,  -18,
    0,    0,    0,    0,    0,   95,  115,    0,    0,  118,
    0, -187,    0, -177,   -4,  -34,   37, -154,  282, -146,
    0,    0,   11, -118,    0,    0,    0,    0, -111, -112,
 -109,    0,    0,    0,  -99,  147,   54,    0,  -23,    0,
    0,  109,    0,   54,  131,   94,   42, -187,    0,  -90,
    0,    0,    0,    0,    0,    0,  -95,    0, -118,    0,
   60, -158, -205,    0,    0,  -65, -235,    0, -235,  -69,
    0,   30,  121,  133,  136,  139,  147,    0,  147,    0,
    0,    0,    0,    0,    0,    0,  147,  147,    0,  147,
  217,    0,    0,    0,   74,    0, -187, -187,    0,  -69,
    0,    0,    0,    0,    0,  -23,    0,  -23,    0,    0,
    0,    0,   51,   54,   54,   54,   54,    0,    0, -187,
    0, -235,    0,    0,
};
final static short yyrindex[] = {                         1,
  -39,  162,    0,    0,    0,  -85,    0,  -85,    0,  188,
    0,    0,    0,    0,  166,  238,  258,    0,    0,    0,
    0,    0,  -61,    0,    0,    0,    0,    0,  -85,  -82,
    0,    0,    0,  -82,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -61,    0,    0,    0,    0,    0,    0,  -82,    0,
    0,    0,    0, -100,    0,    0,    0,    0,    0,  -61,
  197,    0,    0,    0,    0,    0,  -59,    0,  -16,    0,
    0,    0,    0,  207,    0,    0,    0,  -61,    0,  -61,
    0,    0,    0,    0,    0,    0,    0,    0, -100,    0,
    0,    0,  -85,    0,    0,  292,    0,    0,    0,  -61,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -61,    0,    0,    0,    0,    0,  -85,  -91,    0,  -61,
    0,    0,    0,    0,    0,    8,    0,   31,    0,    0,
    0,    0,    0,   52,   63,   73,   84,    0,    0,  -62,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    3,   83,    0,  480,   10,    0,    0,    0,    0,  103,
    0,  120,    0,    0,    0,    0,    0,    0,    0,    0,
  -11,  171,  -53,    0,  -94,    0,    0,    0,  125,    0,
   -7,  512,   15,   27,    0,    0,
};
final static int YYTABLESIZE=642;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         41,
    1,   92,   92,   92,   27,   92,   94,   92,   29,   11,
   52,   50,  141,   48,  142,   21,  107,   20,  115,   92,
  105,   50,   29,  116,   80,   22,   80,  106,   80,   69,
   45,   19,   53,   30,  132,   84,   36,   21,   86,   62,
   19,   40,   80,   62,   68,   46,   91,    6,   78,   47,
   78,    1,   78,   52,   88,   28,  143,   42,    2,    3,
   19,   89,   28,   41,  112,   41,   78,  164,   62,    1,
  144,   79,  113,   79,  114,   79,    2,    3,   43,   53,
    4,    5,  129,   92,   44,   92,  161,   26,   22,   79,
   34,  163,   60,  113,   55,  114,  113,   89,  114,  133,
   37,   38,   64,   59,  137,  153,   80,  154,   80,    4,
    5,   59,   56,   58,    1,  155,  156,   57,  157,   53,
   92,    2,    3,   96,   57,    4,    5,  146,  148,   95,
   78,   98,   78,   99,   76,   65,  113,  100,  114,   75,
  133,  150,  152,  105,    1,  104,  109,   62,  117,   53,
  106,    2,    3,   79,   76,   79,   29,   76,  111,   75,
   76,   29,   75,   29,   29,   75,    1,   27,   27,   62,
   32,  118,   76,    2,    3,   76,   32,   75,   76,   32,
   75,    1,  134,   75,  136,  138,   76,    1,    2,    3,
  139,   75,    4,    5,    2,    3,   41,   41,  159,   32,
   32,  135,   41,   41,   41,   41,   41,  131,   41,   41,
   71,  130,    0,    0,    0,    0,   92,   92,   41,  160,
   25,   93,   92,   92,   92,   92,   92,   49,   92,   92,
   92,   92,   92,   92,   92,   92,   92,   70,   92,   80,
   80,   66,   67,    0,    0,   80,   80,   80,   80,   80,
    0,   80,   80,   80,   80,   80,   80,   80,   80,   80,
    0,   80,   32,   78,   78,   40,    0,    0,   58,   78,
   78,   78,   78,   78,    0,   78,   78,   78,   78,   78,
   78,   78,   78,   78,   64,   78,   79,   79,   37,    0,
   37,    0,   79,   79,   79,   79,   79,  128,   79,   79,
   79,   79,   79,   79,   79,   79,   79,   60,   79,    0,
    9,  120,  121,  122,  123,  124,  125,  126,   59,   45,
    0,   60,   60,   60,   60,   60,   60,   60,   58,   40,
    0,   40,   59,   59,   59,   59,   59,   59,   59,   57,
    0,  158,   58,   58,   58,   58,   58,   58,   58,  119,
   72,    1,   73,   57,   57,   57,   57,   57,   57,   57,
   39,   74,   39,  120,  121,  122,  123,  124,  125,  126,
   83,    1,   73,   85,    1,   73,  145,    1,   73,    0,
   38,   74,   38,    0,   74,    0,    0,   74,  147,    1,
   73,  149,    1,   73,  151,    1,   73,   60,    0,   74,
    1,    0,   74,    1,   73,   74,   97,    2,    3,    0,
    0,    4,    5,   74,   51,    0,    0,    0,   64,    0,
    0,   37,   37,   64,    0,   64,   64,   37,   37,   37,
   37,   37,    0,   37,   37,    0,    0,    0,    0,    0,
    0,    0,    0,   37,    9,    0,    0,    0,    0,    9,
    0,    9,    9,   45,    0,    9,    9,    0,   45,    0,
   45,   45,   40,   40,    0,    7,    0,    0,   40,   40,
   40,   40,   40,    1,   40,   40,    0,    0,    0,   10,
    2,    3,    0,    0,   40,   31,    0,   35,    0,    0,
    0,    0,    0,   39,   39,    0,    0,    0,    0,   39,
   39,   39,   39,   39,    0,   39,   39,    0,   35,    0,
    0,    0,    0,   38,   38,   39,    0,    0,    0,   38,
   38,   38,   38,   38,    0,   38,   38,    0,    0,    1,
    0,   31,    0,    0,    0,   38,    2,    3,    1,    0,
    4,    5,    0,    0,    0,    2,    3,    0,   51,    4,
    5,    0,    0,   51,    0,   51,   51,   78,   78,    0,
    0,   78,    0,    0,    0,    0,    0,   35,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   31,    0,    0,    0,    0,    0,   78,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   35,    0,    0,    0,
    0,    0,    0,    0,   78,   78,   78,   78,   78,    0,
   78,    0,    0,    0,    0,    0,    0,    0,   78,   78,
    0,   78,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         59,
    0,   41,   42,   43,   40,   45,   41,   47,    6,    0,
  123,   40,  107,   21,  109,  256,   70,  277,   42,   59,
  256,   40,  123,   47,   41,  266,   43,  263,   45,   44,
  256,  123,   23,  125,  125,   47,  278,  123,   50,   30,
  123,  262,   59,   34,   59,  271,   54,  123,   41,  275,
   43,  257,   45,  123,   52,   91,  110,   59,  264,  265,
  123,   52,  125,  123,   76,  125,   59,  162,   59,  257,
   41,   41,   43,   43,   45,   45,  264,  265,   59,   70,
  268,  269,   41,  123,   59,  125,  140,  123,  266,   59,
    8,   41,   41,   43,  261,   45,   43,   88,   45,   90,
  256,  257,  123,   41,  102,  117,  123,  119,  125,  268,
  269,   29,  261,   41,  257,  127,  128,  261,  130,  110,
  125,  264,  265,  278,   41,  268,  269,  113,  114,   93,
  123,  278,  125,  123,   40,  278,   43,  256,   45,   45,
  131,  115,  116,  256,  257,  257,  256,  138,   40,  140,
  263,  264,  265,  123,   40,  125,  257,   40,  258,   45,
   40,  262,   45,  264,  265,   45,  257,  268,  269,  160,
  262,   41,   40,  264,  265,   40,  262,   45,   40,  262,
   45,  257,  278,   45,  125,  103,   40,  257,  264,  265,
  256,   45,  268,  269,  264,  265,  256,  257,  125,  262,
  262,   99,  262,  263,  264,  265,  266,   88,  268,  269,
   40,   87,   -1,   -1,   -1,   -1,  256,  257,  278,  137,
  256,  256,  262,  263,  264,  265,  266,  256,  268,  269,
  270,  271,  272,  273,  274,  275,  276,  256,  278,  256,
  257,  256,  257,   -1,   -1,  262,  263,  264,  265,  266,
   -1,  268,  269,  270,  271,  272,  273,  274,  275,  276,
   -1,  278,  262,  256,  257,   59,   -1,   -1,  125,  262,
  263,  264,  265,  266,   -1,  268,  269,  270,  271,  272,
  273,  274,  275,  276,  123,  278,  256,  257,  123,   -1,
  125,   -1,  262,  263,  264,  265,  266,  256,  268,  269,
  270,  271,  272,  273,  274,  275,  276,  256,  278,   -1,
  123,  270,  271,  272,  273,  274,  275,  276,  256,  123,
   -1,  270,  271,  272,  273,  274,  275,  276,  256,  123,
   -1,  125,  270,  271,  272,  273,  274,  275,  276,  256,
   -1,  125,  270,  271,  272,  273,  274,  275,  276,  256,
  256,  257,  258,  270,  271,  272,  273,  274,  275,  276,
  123,  267,  125,  270,  271,  272,  273,  274,  275,  276,
  256,  257,  258,  256,  257,  258,  256,  257,  258,   -1,
  123,  267,  125,   -1,  267,   -1,   -1,  267,  256,  257,
  258,  256,  257,  258,  256,  257,  258,  125,   -1,  267,
  257,   -1,  267,  257,  258,  267,  125,  264,  265,   -1,
   -1,  268,  269,  267,  123,   -1,   -1,   -1,  257,   -1,
   -1,  256,  257,  262,   -1,  264,  265,  262,  263,  264,
  265,  266,   -1,  268,  269,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  278,  257,   -1,   -1,   -1,   -1,  262,
   -1,  264,  265,  257,   -1,  268,  269,   -1,  262,   -1,
  264,  265,  256,  257,   -1,  278,   -1,   -1,  262,  263,
  264,  265,  266,  257,  268,  269,   -1,   -1,   -1,    0,
  264,  265,   -1,   -1,  278,    6,   -1,    8,   -1,   -1,
   -1,   -1,   -1,  256,  257,   -1,   -1,   -1,   -1,  262,
  263,  264,  265,  266,   -1,  268,  269,   -1,   29,   -1,
   -1,   -1,   -1,  256,  257,  278,   -1,   -1,   -1,  262,
  263,  264,  265,  266,   -1,  268,  269,   -1,   -1,  257,
   -1,   52,   -1,   -1,   -1,  278,  264,  265,  257,   -1,
  268,  269,   -1,   -1,   -1,  264,  265,   -1,  257,  268,
  269,   -1,   -1,  262,   -1,  264,  265,   46,   47,   -1,
   -1,   50,   -1,   -1,   -1,   -1,   -1,   88,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  102,   -1,   -1,   -1,   -1,   -1,   76,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  137,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  113,  114,  115,  116,  117,   -1,
  119,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  127,  128,
   -1,  130,
};
}
final static short YYFINAL=7;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"T_IDENTIFIER","T_CONST","T_UINT",
"T_LONG","T_STRING","T_RW_IF","T_RW_ELSE","T_RW_DO","T_RW_PRINT","T_RW_WHILE",
"T_RW_TOLONG","T_RW_LONG","T_RW_UINT","T_GT","T_EQ","T_LT","T_BEQ","T_GEQ",
"T_ASSIGN","T_DISTINCT","T_PLUS_PLUS","T_ENDOFFILE",
};
final static String yyrule[] = {
"$accept : programa",
"programa :",
"programa : '{' declaraciones conjunto_sentencias '}' T_ENDOFFILE",
"programa : '{' declaraciones '}' T_ENDOFFILE",
"programa : '{' conjunto_sentencias '}' T_ENDOFFILE",
"programa : declaracion_sentencia T_ENDOFFILE",
"programa : declaraciones conjunto_sentencias T_ENDOFFILE",
"declaracion_sentencia : declaracion",
"declaracion_sentencia : sentencia",
"declaraciones : declaracion",
"declaraciones : declaraciones declaracion",
"declaracion : d_variable",
"d_variable : tipo lista_identificadores ';'",
"d_variable : tipo lista_identificadores error",
"d_variable : tipo error",
"lista_identificadores : T_IDENTIFIER",
"lista_identificadores : lista_identificadores ',' T_IDENTIFIER",
"lista_identificadores : lista_identificadores T_IDENTIFIER",
"conjunto_sentencias : sentencia",
"$$1 :",
"conjunto_sentencias : conjunto_sentencias $$1 '{' ambito '}'",
"$$2 :",
"conjunto_sentencias : $$2 '{' ambito '}'",
"conjunto_sentencias : conjunto_sentencias sentencia",
"conjunto_sentencias : conjunto_sentencias declaracion",
"sentencias : sentencia",
"sentencias : sentencias sentencia",
"$$3 :",
"ambito : $$3 declaraciones conjunto_sentencias",
"$$4 :",
"ambito : $$4 conjunto_sentencias",
"ambito : error",
"$$5 :",
"sentencia : $$5 if",
"sentencia : asignacion ';'",
"sentencia : do ';'",
"sentencia : impresion ';'",
"sentencia : asignacion",
"sentencia : impresion",
"sentencia : do",
"asignacion : lado_izquierdo T_ASSIGN exp_ar",
"asignacion : lado_izquierdo T_EQ exp_ar",
"asignacion : lado_izquierdo T_ASSIGN error",
"asignacion : lado_izquierdo T_EQ error",
"asignacion : lado_izquierdo error",
"$$6 :",
"$$7 :",
"if : T_RW_IF condicion $$6 bloque $$7 else",
"if : T_RW_IF condicion error else",
"if : T_RW_IF error bloque else",
"if : T_RW_IF error else",
"$$8 :",
"else : T_RW_ELSE $$8 bloque",
"else : T_RW_ELSE error",
"else : error",
"condicion : '(' comparacion ')'",
"condicion : '(' error ')'",
"comparacion : comparacion sgn_cmp exp_ar",
"comparacion : comparacion error exp_ar",
"comparacion : exp_ar sgn_cmp exp_ar",
"comparacion : exp_ar error exp_ar",
"bloque : '{' sentencias '}'",
"bloque : sentencia",
"bloque : '{' declaraciones sentencias '}'",
"$$9 :",
"do : T_RW_DO $$9 bloque while",
"do : T_RW_DO error while",
"do : T_RW_DO while",
"while : T_RW_WHILE condicion",
"while : T_RW_WHILE error",
"lado_izquierdo : variable",
"sgn_cmp : T_GT",
"sgn_cmp : T_EQ",
"sgn_cmp : T_LT",
"sgn_cmp : T_BEQ",
"sgn_cmp : T_GEQ",
"sgn_cmp : T_ASSIGN",
"sgn_cmp : T_DISTINCT",
"exp_ar : exp_ar '+' term",
"exp_ar : exp_ar '-' term",
"exp_ar : term",
"exp_ar : exp_ar '+' error",
"exp_ar : exp_ar '-' error",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"term : term '*' error",
"term : term '/' error",
"factor : variable",
"factor : constante",
"factor : conversion '(' exp_ar ')'",
"factor : '(' exp_ar ')'",
"variable : T_IDENTIFIER",
"variable : T_IDENTIFIER T_PLUS_PLUS",
"constante : T_CONST",
"constante : '-' T_CONST",
"tipo : T_RW_UINT",
"tipo : T_RW_LONG",
"conversion : T_RW_TOLONG",
"impresion : T_RW_PRINT '(' T_STRING ')'",
"impresion : T_RW_PRINT '[' T_STRING ']'",
"impresion : T_RW_PRINT '{' T_STRING '}'",
"impresion : T_RW_PRINT '(' T_STRING error",
"impresion : T_RW_PRINT error",
};

//#line 521 "grammar.y"





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




//#line 693 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror(/*"syntax error"*/"");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror(""/*stack underflow. aborting..."*/);  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror(""/*Stack underflow. aborting..."*/);  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 15:
//#line 104 "grammar.y"
{  updateDeclaration(((Row)val_peek(0).obj),tipo_identificador);    }
break;
case 16:
//#line 106 "grammar.y"
{  updateDeclaration((Row)(val_peek(0).obj),tipo_identificador);    }
break;
case 19:
//#line 113 "grammar.y"
{
                                              decorator.nroAnidamiento = decorator.nroAnidamiento + 1;
                                              if ( decorator.nroAnidamiento == 1 ) {
                                                   decorator.nroAmbito  = aux;
                                                   decorator.nroAmbito++;

                                                   }
                                             }
break;
case 20:
//#line 120 "grammar.y"
{ if ( decorator.nroAnidamiento >= 2  )
                                                                     syntaxError("No se permite anidamiento de ambitos.");
                                                                     decorator.nroAnidamiento=decorator.nroAnidamiento - 1;
                                                                     if ( decorator.nroAnidamiento < 1 ) {
                                                                                    decorator.nroAnidamiento= 0;
                                                                                    aux = decorator.nroAmbito;
                                                                                    decorator.nroAmbito = 0;
                                                                                  }
                                                                     }
break;
case 21:
//#line 130 "grammar.y"
{ syntaxError("Se esperan sentencias ejecutables antes de la declaraci�n de un �mbito.");
                          decorator.nroAnidamiento = decorator.nroAnidamiento + 1;
                                  if ( decorator.nroAnidamiento == 1 ){
                                                                   decorator.nroAmbito  = aux;
                                                                   decorator.nroAmbito ++;

                                                                }
                         }
break;
case 22:
//#line 137 "grammar.y"
{ if ( decorator.nroAnidamiento >= 2  )
                                                            syntaxError("No se permite anidamiento de ambitos.");
                                                            decorator.nroAnidamiento=decorator.nroAnidamiento - 1;
                                                            if ( decorator.nroAnidamiento < 1 ){
                                                                                  decorator.nroAnidamiento = 0;
                                                                                  aux = decorator.nroAmbito;
                                                                                  decorator.nroAmbito = 0;
                                                              }


                                            }
break;
case 24:
//#line 149 "grammar.y"
{ syntaxError("Las declaraciones deben preceder a las sentencias ejecutables."); }
break;
case 27:
//#line 158 "grammar.y"
{ informarSentencia("Declaraci�n de ambito.");}
break;
case 29:
//#line 160 "grammar.y"
{ informarSentencia("Declaraci�n de ambito.");}
break;
case 32:
//#line 166 "grammar.y"
{ informarSentencia("Sentencia IF."); }
break;
case 37:
//#line 170 "grammar.y"
{       syntaxError("Se espera ; al finalizar la sentencia.")  ;}
break;
case 38:
//#line 171 "grammar.y"
{       syntaxError("Se espera ; al finalizar la sentencia.")  ;}
break;
case 39:
//#line 172 "grammar.y"
{       syntaxError("Se espera ; al finalizar la sentencia.") ;}
break;
case 40:
//#line 176 "grammar.y"
{  
                                                                  Row token=(Row)val_peek(1).obj;
                                                                  Terceto terceto = new Terceto (token.getToken(),(Row)val_peek(2).obj,val_peek(0).obj);
                                                                  generadorTercetos.add(terceto);
                                                                  /*System.out.println(terceto);*/
                                                                  yyval.obj=(Row)val_peek(2).obj;


                                                              }
break;
case 45:
//#line 193 "grammar.y"
{  
                                                          /* acá pasa la condición :*/
                                                          /* se genera el BF y se apila .*/
                                                         
                                                          Terceto terceto=new Terceto("BF",new Completable(),new Completable());
                                                          generadorTercetos.apilar(terceto);
                                                          generadorTercetos.add(terceto);
                                                          /*$$.obj=terceto;   */
                                                          yyval.obj=new Referencia(this.generadorTercetos,terceto.getId());
                                                    }
break;
case 46:
//#line 205 "grammar.y"
{ 
                                                          
                                                          Terceto BF=(Terceto)generadorTercetos.desapilar();
                                                          Terceto BI = new Terceto("BI",new Completable(),new Completable());
                                                          generadorTercetos.add(BI);
                                                          generadorTercetos.apilar(BI);
                                                          Referencia salto_false=new Referencia(this.generadorTercetos,generadorTercetos.getNextTercetoId());
                                                          generadorTercetos.completarTerceto(BF,salto_false);

                                                      }
break;
case 48:
//#line 218 "grammar.y"
{     syntaxError("Sentencia IF:Se espera { en el bloque de sentencias.");}
break;
case 49:
//#line 219 "grammar.y"
{ syntaxError("Sentencia IF:Se espera ( ) ");}
break;
case 51:
//#line 225 "grammar.y"
{ 

                                                         }
break;
case 52:
//#line 229 "grammar.y"
{ 
                                                          /* acá se completa el statement*/
                                                          /* se desapila y se completa el BI*/
                                                          Terceto BI=(Terceto)generadorTercetos.desapilar();
                                                          Referencia salto_true=new Referencia(this.generadorTercetos,generadorTercetos.getNextTercetoId());
                                                          generadorTercetos.completarTerceto(BI,salto_true);
                                                          
                                                        }
break;
case 53:
//#line 238 "grammar.y"
{ syntaxError("Sentencia IF-ELSE:Se espera sentencia o bloque de sentencias."); }
break;
case 54:
//#line 240 "grammar.y"
{ syntaxError("Sentencia IF-ELSE:Se espera ELSE o el bloque de sentencias es erróneo.");}
break;
case 55:
//#line 245 "grammar.y"
{
                                                      
                                                        /*Terceto terceto=new Terceto();  */
                                                        yyval=val_peek(1);
                                                     
                                                     }
break;
case 56:
//#line 252 "grammar.y"
{syntaxError("Se esperaba ( ) en lugar de { }");}
break;
case 57:
//#line 256 "grammar.y"
{  
                                                          Row operador = (Row)val_peek(1).obj;
                                                          Terceto terceto = new Terceto(operador.getToken(),val_peek(2).obj,val_peek(0).obj);
                                                          generadorTercetos.add(terceto);
                                                          /*System.out.println(terceto);*/
                                                          /*$$.obj=terceto;*/
                                                          yyval.obj=new Referencia(this.generadorTercetos,terceto.getId());
                                                       }
break;
case 58:
//#line 266 "grammar.y"
{ syntaxError("Operador de comparación no válido.");}
break;
case 59:
//#line 268 "grammar.y"
{  
                                                          Row operador = (Row)val_peek(1).obj;
                                                          Terceto terceto = new Terceto(operador.getToken(),val_peek(2).obj,val_peek(0).obj);
                                                          generadorTercetos.add(terceto);
                                                          /*System.out.println(terceto);*/
                                                          /*$$.obj=terceto;*/
                                                          yyval.obj=new Referencia(this.generadorTercetos,terceto.getId());
                                                     }
break;
case 63:
//#line 282 "grammar.y"
{ syntaxError("No se permiten declaraciones dentro de un bloque de sentencias.");}
break;
case 64:
//#line 287 "grammar.y"
{
                                                                 
                                                                 Referencia inicio_loop = new Referencia(this.generadorTercetos,generadorTercetos.getNextTercetoId());
                                                                 generadorTercetos.apilar(inicio_loop);


                                                                }
break;
case 66:
//#line 297 "grammar.y"
{ syntaxError("Sentencia DO WHILE:Bloque de sentencias erroneo.");  }
break;
case 67:
//#line 298 "grammar.y"
{ syntaxError("Sentencia DO WHILE:Se espera bloque de sentencias.");}
break;
case 68:
//#line 302 "grammar.y"
{ 

                                                    Terceto BF=new Terceto("BF",new Completable(),new Completable());
                                                    Terceto BI=new Terceto("BI",new Completable(),new Completable());
                                                    generadorTercetos.add(BF);
                                                    generadorTercetos.add(BI);
                                                    Referencia salto_false=new Referencia(this.generadorTercetos,generadorTercetos.getNextTercetoId());
                                                    Referencia inicio_loop=(Referencia)generadorTercetos.desapilar();
                                                    generadorTercetos.completarTerceto(BF,salto_false);
                                                    generadorTercetos.completarTerceto(BI,inicio_loop);

                                                  }
break;
case 69:
//#line 316 "grammar.y"
{ syntaxError("Sentencia DO WHILE: Se espera condición. ");}
break;
case 70:
//#line 319 "grammar.y"
{  
                                                     
                                                    yyval=val_peek(0);
                                                }
break;
case 76:
//#line 332 "grammar.y"
{ syntaxError("Se esperaba = en lugar de := "); }
break;
case 78:
//#line 337 "grammar.y"
{  
                                                      
                                                      Terceto terceto = new Terceto ("+",val_peek(2).obj,val_peek(0).obj);
                                                      /*System.out.println(terceto);*/
                                                      generadorTercetos.add(terceto);
                                                      yyval.obj=new Referencia(this.generadorTercetos,terceto.getId());
                                                  }
break;
case 79:
//#line 346 "grammar.y"
{  
                                                      Terceto terceto = new Terceto ("-",val_peek(2).obj,val_peek(0).obj);
                                                      /*System.out.println(terceto);*/
                                                      generadorTercetos.add(terceto);
                                                      yyval.obj=new Referencia(this.generadorTercetos,terceto.getId());

                                                    }
break;
case 80:
//#line 354 "grammar.y"
{    
                                            yyval=val_peek(0);   
                                         }
break;
case 81:
//#line 358 "grammar.y"
{ syntaxError("Expresión aritmética: Operando no válido."); }
break;
case 82:
//#line 359 "grammar.y"
{ syntaxError("Expresión aritmética: Operando no válido."); }
break;
case 83:
//#line 363 "grammar.y"
{  
                                          Terceto terceto = new Terceto ("*",val_peek(2).obj,val_peek(0).obj);
                                          /*System.out.println(terceto);*/
                                          generadorTercetos.add(terceto);
                                          yyval.obj=new Referencia(this.generadorTercetos,terceto.getId());
                                        }
break;
case 84:
//#line 370 "grammar.y"
{ 
                                            Terceto terceto = new Terceto ("/",val_peek(2).obj,val_peek(0).obj);
                                           /* System.out.println(terceto);*/
                                            generadorTercetos.add(terceto);
                                            yyval.obj=new Referencia(this.generadorTercetos,terceto.getId());
                                          }
break;
case 85:
//#line 377 "grammar.y"
{ 
                                          yyval=val_peek(0); 

                                        }
break;
case 86:
//#line 381 "grammar.y"
{ syntaxError("Expresión aritmética: Operando no válido."); }
break;
case 87:
//#line 382 "grammar.y"
{ syntaxError("Expresión aritmética: Operando no válido."); }
break;
case 88:
//#line 388 "grammar.y"
{  yyval = val_peek(0);   }
break;
case 89:
//#line 389 "grammar.y"
{  yyval = val_peek(0);   }
break;
case 90:
//#line 391 "grammar.y"
{  
                                                                    Row token=(Row)val_peek(3).obj;
                                                                    Terceto terceto = new Terceto (token.getToken(),new Completable(),val_peek(1).obj);
                                                                    /*System.out.println(terceto);*/
                                                                    generadorTercetos.add(terceto);
                                                                    /*$$.obj=terceto;   */
                                                                    yyval.obj=new Referencia(this.generadorTercetos,terceto.getId());
                                                                  }
break;
case 91:
//#line 400 "grammar.y"
{
                                                                  syntaxError("No se permiten expresiones aritm�ticas entre par�ntesis.");
                                                               }
break;
case 92:
//#line 407 "grammar.y"
{  
                                                               
                                                               Row row = verifyDeclaration (decorator,(val_peek(0).obj));
                                                               yyval.obj=row;
                                                               
                                                               }
break;
case 93:
//#line 415 "grammar.y"
{  
                                                              Row id = (Row)val_peek(1).obj;
                                                              if (id.getType().compareTo("long")!=0){
                                                                Lexer.showError("La operación ++ , sólo está permitida para tipos long .");
                                                              }
                                                              Row constant = new RowConst("const","1","long");
                                                              Terceto terceto_plus = new Terceto ("+",val_peek(1).obj,constant);
                                                              generadorTercetos.add(terceto_plus);
                                                              Terceto terceto= new Terceto (":=",val_peek(1).obj,new Referencia(this.generadorTercetos,terceto_plus.getId()));
                                                              generadorTercetos.add(terceto);
                                                              /*System.out.println(terceto);*/
                                                              /*$$.obj=terceto;*/
                                                              yyval.obj=new Referencia(this.generadorTercetos,terceto.getId());

                                                            }
break;
case 94:
//#line 434 "grammar.y"
{  

                                                           
                                                      long val = 0;
                                                      Row row= (Row)val_peek(0).obj;
                                                      try {
                                                            val = Long.parseLong(row.getLexeme());
                                                            if ( val > 2147483647 )
                                                                 syntaxError(" Constante long " + row.getLexeme() + "  fuera de rango.");
                                                            

                                                            
                                                        }
                                                        catch(NumberFormatException e){
                                                          e.printStackTrace();
                                                        } 
                                                      
                                                      yyval.obj=row;
                                                     /* System.out.println(" Constante  : "  +  $$.obj);*/


                                                      }
break;
case 95:
//#line 458 "grammar.y"
{
                                                         
                                                         long val = 0;
                                                         Row row= (Row)val_peek(0).obj;
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

                                                         yyval.obj=row;       
                                                        /* System.out.println(" Constante negativa : "  +  $$.obj);*/

                                                     
                                                     }
break;
case 96:
//#line 489 "grammar.y"
{ tipo_identificador = "uint"; }
break;
case 97:
//#line 491 "grammar.y"
{ tipo_identificador = "long"; }
break;
case 98:
//#line 494 "grammar.y"
{ yyval = val_peek(0); }
break;
case 99:
//#line 503 "grammar.y"
{ 
                                                      
                                                      Row token  = (Row)val_peek(3).obj;
                                                      Row lexema = (Row)val_peek(1).obj;
                                                      Terceto terceto = new Terceto(token.getToken(),new Completable(),lexema);
                                                      generadorTercetos.add(terceto);
                                                      /*System.out.println(terceto);*/

                                                      }
break;
case 100:
//#line 513 "grammar.y"
{ syntaxError("Sentencia PRINT:Se espera ( y ) en lugar de [ ]."); }
break;
case 101:
//#line 514 "grammar.y"
{ syntaxError("Sentencia PRINT:Se espera ( y ) en lugar de { }."); }
break;
case 102:
//#line 515 "grammar.y"
{ syntaxError("Sentencia PRINT:Se espera )");}
break;
case 103:
//#line 516 "grammar.y"
{ syntaxError("Se espera ( luego de PRINT."); }
break;
//#line 1299 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
