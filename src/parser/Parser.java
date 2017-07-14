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

//#line 26 "Parser.java"




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
    4,    6,    6,    6,    8,    8,    8,    2,    2,    2,
    2,    2,   10,   10,    9,    9,    9,    5,    5,    5,
    5,    5,    5,    5,   11,   11,   11,   11,   11,   14,
   14,   14,   14,   19,   19,   19,   17,   17,   20,   20,
   20,   20,   18,   18,   18,   13,   13,   13,   22,   22,
   15,   21,   21,   21,   21,   21,   21,   21,   16,   16,
   16,   16,   16,   24,   24,   24,   24,   24,   25,   25,
   25,   25,   23,   23,   26,   26,    7,    7,   27,   12,
   12,   12,   12,   12,
};
final static short yylen[] = {                            2,
    0,    5,    4,    3,    2,    3,    1,    1,    1,    2,
    1,    3,    3,    2,    1,    3,    2,    1,    4,    3,
    2,    2,    1,    2,    2,    1,    1,    1,    1,    1,
    1,    2,    2,    2,    3,    3,    3,    3,    2,    4,
    4,    4,    3,    2,    2,    1,    3,    3,    3,    3,
    3,    3,    3,    1,    4,    3,    3,    2,    2,    2,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    3,
    1,    3,    3,    3,    3,    1,    3,    3,    1,    1,
    4,    3,    1,    2,    1,    2,    1,    1,    1,    4,
    4,    4,    4,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   88,   87,    0,    0,    0,    0,
    0,    8,   11,    0,    0,    0,    0,   31,    0,   61,
   84,    0,    0,    0,    0,    0,    0,   54,    0,   58,
   94,    0,    0,    0,    0,    0,    0,    9,   18,    0,
   10,    5,   14,   15,    0,   32,   34,   33,   39,    0,
    0,   46,    0,    0,   43,    0,   85,   89,    0,    0,
    0,    0,   79,    0,   76,   80,    0,    0,    0,   57,
   60,   59,    0,   23,    0,   56,    0,    0,    0,   27,
    0,    0,    0,    0,    0,    0,    4,   22,   21,    6,
   13,   17,   12,    0,   38,    0,   37,    0,   45,   44,
   42,   48,   86,    0,    0,   62,   63,   64,   65,   66,
   67,   68,    0,    0,    0,    0,   47,    0,    0,    0,
    0,   41,   40,    0,   53,   24,   92,   93,   90,   91,
    0,   20,    3,    0,    0,   16,   82,    0,   72,    0,
   73,    0,    0,    0,    0,   77,   74,   78,   75,    0,
   55,    2,   19,   81,
};
final static short yydgoto[] = {                          8,
   81,   82,   10,   88,   39,   13,   14,   45,   83,   75,
   15,   16,   17,   18,   19,   61,   24,   29,   55,   62,
  115,   30,   20,   64,   65,   66,   67,
};
final static short yysindex[] = {                       321,
 -256,  -30, -116,  -37,    0,    0,  334,    0,  334, -241,
    0,    0,    0, -194,  -14,   -7,   -3,    0, -242,    0,
    0,  142,   92,  -85, -206,  -20, -166,    0, -206,    0,
    0, -195, -191, -189,  -55,  290,  299,    0,    0,  185,
    0,    0,    0,    0,  -16,    0,    0,    0,    0,  112,
  115,    0,  -74, -221,    0,   37,    0,    0, -179,  144,
   91,   39,    0,  -34,    0,    0,   41, -221, -221,    0,
    0,    0, -166,    0, -103,    0,  -42,  -36,   -5,    0,
  334,  348,  -33, -183,  312,  -55,    0,    0,    0,    0,
    0,    0,    0, -160,    0,  -19,    0,  -19,    0,    0,
    0,    0,    0,   -9,  144,    0,    0,    0,    0,    0,
    0,    0,  118,  130,  144,  144,    0,  144,  133,  136,
  144,    0,    0,  308,    0,    0,    0,    0,    0,    0,
  348,    0,    0, -178,  -21,    0,    0,  -19,    0,  -34,
    0,  -34,  -19,  -19,  -19,    0,    0,    0,    0,   16,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                       106,
  -41,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  273,    0,    0,    0,  -70,  163,  235,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -18,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -17,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  204,    0,  258,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -15,    0,    0,    0,    0,    0,    0,   49,    0,    5,
    0,   28,   60,   70,   81,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   12,    8,    0,   58,  584,    0,    0,    0,   23,   40,
    0,    0,    0,    0,    0,  428,   86,  -13,  -38,    0,
   52,   22,  569,  -39,  -43,    0,    0,
};
final static int YYTABLESIZE=715;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         83,
   83,   83,   33,   83,  129,   83,   27,  119,   54,   23,
   69,    9,  120,   49,   37,  101,   40,   83,   36,   23,
   21,  125,   71,  113,   71,  114,   71,   94,   50,  122,
  123,  137,   51,  113,   52,  114,   42,   27,   73,  100,
   71,   53,   93,   85,   46,   69,   70,   69,   27,   69,
   76,   47,   28,   34,   28,   48,  154,   11,  113,   26,
  114,   43,   44,   69,   38,   77,   41,   35,   70,   78,
   70,   79,   70,  140,  142,  147,  149,  102,  103,  117,
  121,   83,  127,   83,   38,   32,   70,  130,  131,   52,
    1,  132,   38,   41,  133,    2,  136,    3,    4,  152,
   51,    5,    6,  153,   71,    1,   71,   26,  135,   25,
   50,   72,  124,  118,    0,    0,    0,    0,    0,    0,
    0,   49,    0,    0,    0,    0,    0,   69,    0,   69,
   41,   60,    0,  113,    0,  114,   59,    0,   41,   25,
    1,    0,    0,   38,    0,    2,    0,    3,    4,   26,
   70,   60,   70,    1,   60,    0,   59,   60,    2,   59,
    3,    4,   59,    0,    0,    0,    0,    0,    0,   60,
   68,    1,   60,    0,   59,   60,    2,   59,    3,    4,
   59,   99,    1,   60,    0,   28,   28,    2,   59,    3,
    4,   28,   28,   28,   28,   28,    0,   28,   28,    0,
   80,    1,    0,    0,    0,    0,    2,   28,    3,    4,
    0,    0,    5,    6,   83,   83,    0,    0,   31,  128,
   83,   83,   83,   83,   83,   22,   83,   83,   83,   83,
   83,   83,   83,   83,   83,   71,   83,   71,   71,   91,
   92,    0,    0,   71,   71,   71,   71,   71,    0,   71,
   71,   71,   71,   71,   71,   71,   71,   71,    0,   71,
   69,   69,   36,    0,   27,    0,   69,   69,   69,   69,
   69,    0,   69,   69,   69,   69,   69,   69,   69,   69,
   69,    0,   69,   70,   70,   29,    0,   29,    0,   70,
   70,   70,   70,   70,  116,   70,   70,   70,   70,   70,
   70,   70,   70,   70,   52,   70,    0,   86,  106,  107,
  108,  109,  110,  111,  112,   51,   35,    0,   52,   52,
   52,   52,   52,   52,   52,   50,   36,    0,   36,   51,
   51,   51,   51,   51,   51,   51,   49,    0,    0,   50,
   50,   50,   50,   50,   50,   50,  105,   56,    1,   57,
   49,   49,   49,   49,   49,   49,   49,   30,   58,   30,
  106,  107,  108,  109,  110,  111,  112,   95,    1,   57,
   97,    1,   57,  139,    1,   57,    0,    0,   58,    0,
   35,   58,   35,    0,   58,  141,    1,   57,  146,    1,
   57,  148,    1,   57,    0,    9,   58,   52,    1,   58,
    1,   57,   58,    2,   53,    3,    4,    0,    0,    0,
   58,    0,   35,    0,   84,    0,    0,    0,   29,   29,
    0,   86,    0,   87,   29,   29,   29,   29,   29,    0,
   29,   29,  151,    0,   86,    0,  134,    0,    0,    0,
   29,    1,    0,    7,    0,    0,    2,    0,    3,    4,
    0,    0,    5,    6,    0,    0,   35,    0,    0,   36,
   36,    0,   90,    0,    0,   36,   36,   36,   36,   36,
   86,   36,   36,    0,    0,    0,    0,   96,   98,    0,
    0,   36,    0,    0,    0,    0,    0,  104,    0,    0,
   30,   30,    0,    0,    0,    0,   30,   30,   30,   30,
   30,    0,   30,   30,    0,    0,    0,    0,    0,    0,
    0,    0,   30,   35,   35,    0,    0,    0,    0,   35,
   35,   35,   35,   35,    0,   35,   35,    0,    0,    9,
    0,    0,  138,    0,    9,   35,    9,    9,    0,    0,
    9,    9,  143,  144,    0,  145,    1,    0,  150,    0,
    7,    2,    0,    3,    4,    1,    0,    5,    6,    0,
    2,    0,    3,    4,    1,    0,    5,    6,    1,    2,
    0,    3,    4,    2,    0,    3,    4,    1,    0,    5,
    6,    0,    2,   12,    3,    4,   28,    0,    5,    6,
    1,   63,    0,    0,    0,    2,    0,    3,    4,    0,
    0,    5,    6,    0,    1,   28,    0,   28,    0,    2,
   74,    3,    4,    0,    0,    5,    6,    0,   63,   63,
   89,    0,    0,   89,    0,    0,    0,    0,   63,    0,
    0,    0,    0,    0,    0,    0,   28,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   74,    0,  126,    0,
    0,    0,    0,    0,    0,   89,    0,    0,   89,    0,
    0,    0,    0,   63,    0,    0,    0,    0,    0,    0,
    0,   63,   63,   63,   63,    0,   63,   63,   63,   63,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  126,    0,    0,
    0,    0,    0,    0,   89,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   40,   45,   41,   47,  123,   42,   22,   40,
   24,    0,   47,  256,    7,   54,    9,   59,    7,   40,
  277,  125,   41,   43,   43,   45,   45,   44,  271,   68,
   69,   41,  275,   43,  256,   45,  278,  123,   27,   53,
   59,  263,   59,   36,   59,   41,   25,   43,  123,   45,
   29,   59,  123,   91,  125,   59,   41,    0,   43,  266,
   45,  256,  257,   59,    7,  261,    9,  123,   41,  261,
   43,  261,   45,  113,  114,  119,  120,   41,  258,   41,
   40,  123,  125,  125,   27,  123,   59,   93,   81,   41,
  257,  125,   35,   36,  278,  262,  257,  264,  265,  278,
   41,  268,  269,  125,  123,    0,  125,  125,   86,  125,
   41,   26,   73,   62,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   41,   -1,   -1,   -1,   -1,   -1,  123,   -1,  125,
   73,   40,   -1,   43,   -1,   45,   45,   -1,   81,  256,
  257,   -1,   -1,   86,   -1,  262,   -1,  264,  265,  266,
  123,   40,  125,  257,   40,   -1,   45,   40,  262,   45,
  264,  265,   45,   -1,   -1,   -1,   -1,   -1,   -1,   40,
  256,  257,   40,   -1,   45,   40,  262,   45,  264,  265,
   45,  256,  257,   40,   -1,  256,  257,  262,   45,  264,
  265,  262,  263,  264,  265,  266,   -1,  268,  269,   -1,
  256,  257,   -1,   -1,   -1,   -1,  262,  278,  264,  265,
   -1,   -1,  268,  269,  256,  257,   -1,   -1,  256,  256,
  262,  263,  264,  265,  266,  256,  268,  269,  270,  271,
  272,  273,  274,  275,  276,  256,  278,  256,  257,  256,
  257,   -1,   -1,  262,  263,  264,  265,  266,   -1,  268,
  269,  270,  271,  272,  273,  274,  275,  276,   -1,  278,
  256,  257,   59,   -1,  123,   -1,  262,  263,  264,  265,
  266,   -1,  268,  269,  270,  271,  272,  273,  274,  275,
  276,   -1,  278,  256,  257,  123,   -1,  125,   -1,  262,
  263,  264,  265,  266,  256,  268,  269,  270,  271,  272,
  273,  274,  275,  276,  256,  278,   -1,  123,  270,  271,
  272,  273,  274,  275,  276,  256,   59,   -1,  270,  271,
  272,  273,  274,  275,  276,  256,  123,   -1,  125,  270,
  271,  272,  273,  274,  275,  276,  256,   -1,   -1,  270,
  271,  272,  273,  274,  275,  276,  256,  256,  257,  258,
  270,  271,  272,  273,  274,  275,  276,  123,  267,  125,
  270,  271,  272,  273,  274,  275,  276,  256,  257,  258,
  256,  257,  258,  256,  257,  258,   -1,   -1,  267,   -1,
  123,  267,  125,   -1,  267,  256,  257,  258,  256,  257,
  258,  256,  257,  258,   -1,  123,  267,  256,  257,  267,
  257,  258,  267,  262,  263,  264,  265,   -1,   -1,   -1,
  267,   -1,  123,   -1,  125,   -1,   -1,   -1,  256,  257,
   -1,  123,   -1,  125,  262,  263,  264,  265,  266,   -1,
  268,  269,  125,   -1,  123,   -1,  125,   -1,   -1,   -1,
  278,  257,   -1,  123,   -1,   -1,  262,   -1,  264,  265,
   -1,   -1,  268,  269,   -1,   -1,  123,   -1,   -1,  256,
  257,   -1,  278,   -1,   -1,  262,  263,  264,  265,  266,
  123,  268,  269,   -1,   -1,   -1,   -1,   50,   51,   -1,
   -1,  278,   -1,   -1,   -1,   -1,   -1,   60,   -1,   -1,
  256,  257,   -1,   -1,   -1,   -1,  262,  263,  264,  265,
  266,   -1,  268,  269,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  278,  256,  257,   -1,   -1,   -1,   -1,  262,
  263,  264,  265,  266,   -1,  268,  269,   -1,   -1,  257,
   -1,   -1,  105,   -1,  262,  278,  264,  265,   -1,   -1,
  268,  269,  115,  116,   -1,  118,  257,   -1,  121,   -1,
  278,  262,   -1,  264,  265,  257,   -1,  268,  269,   -1,
  262,   -1,  264,  265,  257,   -1,  268,  269,  257,  262,
   -1,  264,  265,  262,   -1,  264,  265,  257,   -1,  268,
  269,   -1,  262,    0,  264,  265,    3,   -1,  268,  269,
  257,   23,   -1,   -1,   -1,  262,   -1,  264,  265,   -1,
   -1,  268,  269,   -1,  257,   22,   -1,   24,   -1,  262,
   27,  264,  265,   -1,   -1,  268,  269,   -1,   50,   51,
   37,   -1,   -1,   40,   -1,   -1,   -1,   -1,   60,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   53,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   73,   -1,   75,   -1,
   -1,   -1,   -1,   -1,   -1,   82,   -1,   -1,   85,   -1,
   -1,   -1,   -1,  105,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  113,  114,  115,  116,   -1,  118,  119,  120,  121,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  124,   -1,   -1,
   -1,   -1,   -1,   -1,  131,
};
}
final static short YYFINAL=8;
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
"programa : '{' conjunto_sentencias '}'",
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
"conjunto_sentencias : conjunto_sentencias '{' ambito '}'",
"conjunto_sentencias : '{' ambito '}'",
"conjunto_sentencias : conjunto_sentencias sentencia",
"conjunto_sentencias : conjunto_sentencias declaracion",
"sentencias : sentencia",
"sentencias : sentencias sentencia",
"ambito : declaraciones conjunto_sentencias",
"ambito : conjunto_sentencias",
"ambito : error",
"sentencia : asignacion",
"sentencia : impresion",
"sentencia : do",
"sentencia : if",
"sentencia : asignacion ';'",
"sentencia : do ';'",
"sentencia : impresion ';'",
"asignacion : lado_izquierdo T_ASSIGN exp_ar",
"asignacion : lado_izquierdo T_EQ exp_ar",
"asignacion : lado_izquierdo T_ASSIGN error",
"asignacion : lado_izquierdo T_EQ error",
"asignacion : lado_izquierdo error",
"if : T_RW_IF condicion bloque else",
"if : T_RW_IF condicion error else",
"if : T_RW_IF error bloque else",
"if : T_RW_IF error else",
"else : T_RW_ELSE bloque",
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
"do : T_RW_DO bloque while",
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

//#line 408 "grammar.y"




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
  return  0;//(Short)codes.get("ENDOFFILE");
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



//#line 635 "Parser.java"
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
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
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
              yyerror("Stack underflow. aborting...");  //capital 'S'
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
case 2:
//#line 74 "grammar.y"
{ System.out.println(" Programa aceptado ");}
break;
case 71:
//#line 304 "grammar.y"
{ yyval=val_peek(0);}
break;
//#line 792 "Parser.java"
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
