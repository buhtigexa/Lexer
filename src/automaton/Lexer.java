package automaton;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Hashtable;

import semanticActions.Accumulate;
import semanticActions.Clear;
import semanticActions.Constant;
import semanticActions.Empty;
import semanticActions.ID;
import semanticActions.IllegalCharacter;
import semanticActions.Rewind;
import semanticActions.SemanticAction;
import semanticActions.SequentialAction;
import semanticActions.TComment;
import semanticActions.TString;
import semanticActions.Token;
import semanticActions.WhiteChar;
import symboltable.Row;
import symboltable.SymbolTable;

public class Lexer {

	final static int INVALID_CHAR = 21;
	protected String filePath;
	protected RandomAccessFile raf;
	public String lexeme;
	public final static int MaxState=18;
	public SymbolTable symTable;

	
	
	long position;
	
	public static boolean eof;
	
	public static int line,column,errors,warnings;
	
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int ef=MaxState;
	
	public int e0=0;
	public int e1=1;
	public int e2=2;
	public int e3=3;
	public int e4=4;
	public int e5=5;
	public int e6=6;
	public int e7=7;
	public int e8=8;
	public int e9=9;
	public int e10=10;
	public int e11=11;
	public int e12=12;
	public int e13=13;
	public int e14=14;
	public int e15=15;
	public int e16=16;
	public int e17=17;
	public int e18=18;
	public int e19=19;
	
	public SemanticAction as0 = new Empty(this);
			
	public SemanticAction as1 = new Clear(this);
	
	public SemanticAction as2 = new Accumulate(this);
	
	public SemanticAction as3 = new Rewind(this);
	
	public SemanticAction as4 = new ID(this);
	
	public SemanticAction as5 = new WhiteChar(this);
	
	public SemanticAction as6 = new Constant(this);

	public SemanticAction as7 = new Token(this);
	
	public SemanticAction as8 = new IllegalCharacter(this);
	
	public SemanticAction as9 = new TString(this);
		
	public SemanticAction as10 = new TComment(this);
	
	
	// composite semantic actions 
	
	public SemanticAction as12 = new SequentialAction(this, as1, as2);

	public SemanticAction as34 = new SequentialAction(this, as3, as4);
			
	public SemanticAction as36 = new SequentialAction(this, as3, as6);
	
	public SemanticAction as37 = new SequentialAction(this, as3, as7);
	
	public SemanticAction as38 = new SequentialAction(this, as3, as8);
	
	public SemanticAction as13 = new SequentialAction(this,as1,as3);
	
	public SemanticAction as39 = new SequentialAction(this, as3, as9);
	
	public SemanticAction as87 = new SequentialAction(this, as8, as7);
	
	public SemanticAction as28 = new SequentialAction(this, as2, as8);
	
	public SemanticAction as310 = new SequentialAction(this, as3, as10);
	
	public SemanticAction as810 = new SequentialAction(this, as8, as10);
	
	public SemanticAction as78 = new SequentialAction(this, as7, as8);
	
	public SemanticAction as32 = new SequentialAction(this, as3, as2);
	
	public SemanticAction as84 = new SequentialAction(this, as8, as4);
	
	public SemanticAction as86 = new SequentialAction(this, as8, as6);
	
	public Hashtable<String, String> reservedWords;

	public static PrintWriter errorPrinter;
	public static PrintWriter warningPrinter;
	
	public static boolean printWarning;
	public static boolean printError;
	public static boolean printMsg;
	
	public File errFile, warFile;
	
	
	public int automaton[][]={
			
	/* I MUST REVIEW THIS AUTOMATON, IT'S VERY HARD TO SEE !! 																				// invalid char
			
	/*		0 	  1      2     3     4     5     6     7     8      9    10     11    12     13     14    15    16     17    18    19	20	 21*/
	
/*e0*/ 	{	e2,  e1,    e3,    e8,  e7,   e7,    e10,  e7,  e12,    e7,   e7,   e16,   e7,    e0,   e0,   e0,    e0,   e7,  e7,   e7,  e14,   ef},

/*e1*/ 	{	ef,  e1,    ef,    ef,  ef,   ef,    ef,   ef,   ef,    ef,   ef,    ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,  ef,  ef,   ef},	

/*e2*/ 	{	e2,  ef,    ef,    ef,  ef,   ef,    ef,   ef,   ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},		

/*e3*/ 	{	ef,  ef,    ef,    ef,  ef,   ef,    ef,   ef,   ef,   e4,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},

/*e4*/ 	{	e4,  e4,    e4,    e4,  e4,   e4,    e4,   e4,   e4,   e4,   e5,    e4,    e4,   e4,    e4,   e4,   e4,   e4,   e4,   e4,   e4,   e4},

/*e5*/ 	{	e4,  e4,    e6,    e4,  e4,   e4,    e4,   e4,   e4,   e4,   e4,    e4,    e4,   e4,    e4,   e4,   e4,   e4,   e4,   e4,   e4,   e4},

/*e6*/ 	{	ef,  ef,    ef,    ef,  ef,   ef,    ef,   ef,   ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},


/*e7*/ 	{	ef,  ef,    ef,    ef,  ef,   ef,    ef,   ef,   ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},

/*e8*/ 	{	ef,  ef,    ef,    e9,  ef,   ef,    ef,   ef,   ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},

/*e9*/ 	{	ef,  ef,    ef,    ef,  ef,   ef,    ef,   ef,   ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},

/*e10*/ {	ef,  ef,    ef,    ef,  ef,   ef,    ef,  e11,  e11,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},

/*e11*/ {	ef,  ef,    ef,    ef,  ef,   ef,    ef,   ef,   ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},

/*e12*/	{	ef,  ef,    ef,    ef,  ef,   ef,    ef,  e13,   ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},

/*e13*/	{	ef,  ef,    ef,    ef,  ef,   ef,    ef,   ef,   ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},

/*e14*/	{ e14,  e14,   e14,   e14, e14,  e14,   e14,  e14,  e14,  e14,  e14,   e14,   e14,  e14,   e14,  e14,  e14,  e14,  e14,  e14,  e15,  e14},

/*e15*/	{  ef,   ef,   ef,    ef,   ef,   ef,   ef,   ef,    ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,   ef},

/*e16*/	{  ef,   ef,   ef,    ef,   ef,   ef,   ef,   e17,    ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,  ef},

/*e17*/	{  ef,   ef,   ef,    ef,   ef,   ef,   ef,    ef,    ef,   ef,   ef,    ef,    ef,   ef,    ef,   ef,   ef,   ef,   ef,   ef,   ef,  ef}

	};
	
	
	public SemanticAction actions[][]={

	/*		0     1      2     3      4     5     6     7     8      9    10      11     12     13     14    15    16     17      18    19    20     21*/

/*e0*/ {  as12,  as12,  as12, as12,  as12, as12, as12, as12, as12,  as12,  as12, as12,  as12,   as5,   as5,  as5,   as5,  as12, as12, as12,   as1, as8},

/*e1*/ {  as34,   as2,  as34, as34,  as34, as34, as34, as34, as34,  as34,  as34, as34,  as34,  as34,  as34, as34,  as34,  as34, as34, as34, as34, as84},

/*e2*/ {  as2,  as36,  as36,  as36,  as36, as36, as36, as36, as36,  as36,  as36, as36,  as36,  as36,  as36, as36,  as36,  as36, as36, as36, as86 },

/*e3*/ {  as37,  as37,  as37, as37,  as37, as37, as37, as37, as37,  as12,  as37, as37,  as37,   as37, as37, as37,  as37,  as37,  as37, as37,as37, as87},
	
/*e4*/ {  as2,    as2,   as2,  as2,   as2,  as2,  as2,   as2,  as2,  as2,  as2,   as2,   as2,   as2,   as2,  as2,   as2,  as2,    as2,  as2,  as2,   as28},
	
/*e5*/ {  as3,    as3,   as0,  as3,   as3,  as3,  as3,  as3,  as3,   as3,  as3,   as3,   as3,   as3,   as3,  as3,   as3,  as3,    as3,  as3,  as3,   as8},	
	
/*e6*/ { as310, as310, as310,as310, as310, as310,as310,as310,as310,as310,as310, as310, as310, as310, as310,as310, as310,as310,  as310,as310, as310, as810},

/*e7*/ {  as37,  as37,  as37, as37,  as37,  as37, as37, as37, as37, as37, as37,  as37,  as37,  as37,  as37, as37,  as37,  as37,  as37, as37,   as37,  as78},

/*e8*/ {  as37,  as37,  as12,  as2,  as37,  as37, as37, as37, as37, as37, as37,  as37,  as37,  as37,  as37, as37,  as37,  as37,  as37, as37,   as37,  as78},

/*e9*/ {  as37,  as37,  as37, as37,  as37, as37, as37, as37, as37,  as37, as37,  as37,  as37,  as37,  as37, as37,  as37,  as37,  as37, as37,  as37,  as78},

/*e10*/{  as37,  as37,  as37, as37,  as37, as37, as37,  as2,  as2,  as37, as37,  as37,  as37,  as37,  as37, as37,  as37,  as37,  as37, as37, as37,  as37},

/*e11*/{  as37,  as37,  as12, as37,  as37, as37, as37, as37, as37,  as37, as37,  as37,  as37,  as37,  as37,  as37, as37,  as37,  as37, as37, as37,  as78},

/*e12*/{  as37,  as37,  as37, as37,  as37, as37, as37,  as2, as37,  as37, as37,  as37,  as37,  as37,  as37, as37,  as37,  as37,  as37, as37, as37,   as78},

/*e13*/{  as37,  as37,  as37, as37,  as37, as37,  as37, as37, as37, as37, as37,  as37,  as37,  as37,  as37, as37,  as37,  as37,  as37,  as37, as37,  as78},

/*e14*/{  as2,    as2,  as2,   as2,   as2,  as2,   as2,  as2,  as2,  as2,  as2,   as2,   as2,   as2,   as2,  as2,   as2,   as2,   as2,   as2,  as0,   as8},

/*e15*/{  as39,  as39, as39,  as39,  as39,  as39, as39, as39, as39, as39, as39,  as39,  as39,  as39,  as39,  as39,  as39,  as39,  as39, as39, as39,  as39},

								// aqui trato con la asignacioń: ":="

/*e16*/{ as37, as37, as37, as37, as37, as37, as37,  as2,  as37,  as37, as37,  as37, as37, as37,  as37, as37, as37, as37,  as37, as37,  as37,   as37},
	
/*e17*/{ as37, as37, as37, as37, as37, as37, as37, as37,  as37,  as37, as37,  as37, as37,  as37,  as37, as37, as37, as37,  as37, as37, as37,   as37}

	};
	

	
	//	https://youtu.be/JnaP-nOEF8A
	//	https://youtu.be/GPeiU6RqIjA
	//	https://youtu.be/PIQOqMmU0A8
	
	
	
	public Lexer(String path,SymbolTable symTable,String errorFile,String warningFile,boolean printMsg,boolean printError,boolean printWarning){
		
		
		errFile=new File(errorFile);
		warFile=new File(warningFile);
		
		this.printMsg=printMsg;
		this.printError=printError;
		this.printWarning=printWarning;
		
		try {
			errorPrinter=new PrintWriter(errFile);
		} catch (FileNotFoundException e1) {
		
			e1.printStackTrace();
		}
		try {
			warningPrinter=new PrintWriter(warFile);
		} catch (FileNotFoundException e1) {
			
			
			e1.printStackTrace();
		}
		
		setUp(symTable);
		try {
			raf = new RandomAccessFile(path,"r");
		}
		catch (FileNotFoundException e) {
				e.printStackTrace();
		}
	}
	
	public boolean isReservedWord(String lexeme){
		
		if (reservedWords.containsKey(lexeme)){
			return true;
		}
		return false;
	}
	
	public static int map(char c){
		
		int code=(int)c;
		int map=INVALID_CHAR;// invalid char
		
		// [0-9]
		if ( (code >= 48) && (code <= 57) ){
			map = 0;
		}
		// [a-z] [A-Z]
		if ((code >=65 && code <=90) || (code >=97 && code <=122)){
			map = 1;
		}
		// *
		if (code == 42){
			map = 2;
		}
		// +
		if (code == 43){
			map = 3;
		}
		// -
		if (code == 45){
			map = 4;
		}
		// /
		if (code == 47){
			map = 5;
		}
		// <
		if (code == 60){
			map = 6;
		}
		// =
		if (code == 61){
			map = 7;
		}
		// >
		if (code == 62){
			map = 8;
		}
		// (
		if (code == 40){
			map = 9;
		}
		// )
		if (code == 41){
			map = 10;
		}
		// :
		if (code == 58){
			map = 11;
		}
		// ;
		if (code == 59){
			map = 12;
		}
		// TAB
		if (code == 9){
			map = 13;
		}
		// space
		if (code == 32){
			map = 14;
		}
		// New Line \n 		\n tiene sentido en unix
		if (code == 10) {
			map = 15;
		}
		// Carriage return \r   \n\r tiene sentido en windows
		if (code == 13){
			map = 16;
		}
		// {
		if (code == 123){
			map = 17;
		}
		// }
		if (code == 125){
			map = 18;
		}
		// ,
		if (code==44){
			map = 19;
		}
		
		// '
		if (code ==39){
			map = 20;
		}
		
		return map;
	}
	
	
	public Object getToken(){
		
		Object token=null;
		int e_i=e0;
		int code=0;
		char c=0;
		byte b =0;
		SemanticAction currentAction=null;
		
		try {
			
			while ( e_i != ef ){
				
				b=raf.readByte();
				c=(char)b;
				position++;
				code=map(c);
				currentAction=actions[e_i][code];
				e_i=automaton[e_i][code];
				token=currentAction.execute(c);
				//if (map(c)==INVALID_CHAR){
				//	String message="Caracter inválido "  + c + "codigo de caracter :" + (short)c;
				//	showError(message);
				//}
				if ((e_i==ef) && (token==null)){
					 e_i=e0;
				}
				
			}
		}
		
		catch(EOFException e){
			eof=true;
		}
		catch(IOException e){
		}
		
		//System.out.println("\n\n[LEXER] "  + token);
		return token;
		
	}
	
	public void register(Object token){
		
		if (token==null){
			symTable.add((Row)token);
		}
			
	}

	public void rewind(){
		
		position--;
		column--;
		try {
			raf.seek(position);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void setUp(SymbolTable symTable){
		
		this.symTable=symTable;
		
		
		
		eof=false;
		
		reservedWords=new Hashtable<String, String>();
	
		reservedWords.put("long","long");
		reservedWords.put("uint","uint");
		reservedWords.put("if","if");
		reservedWords.put("else","else");
		reservedWords.put("do","do");
		reservedWords.put("while","while");
		reservedWords.put("print","print");
		reservedWords.put("tolong","tolong");
		position=0;
	
		
	}
	
	public boolean endOfFile(){
		return eof;
		
	}
	
	public static void showWarning(String message){
		
		String warningTxt = "\n -[WARNING]"  +  " linea:columna (" + line + " , " + column + ") :" + message;
		warnings++;
		warningPrinter.write(warningTxt);
		if (printWarning){
			System.out.println(warningTxt);
		}
	}
	

	public static void showError(String message){
		
		String errorTxt = "\n -[ERROR]"  +  "linea:columna (" + line + " , " + column + " ) :" + message;
		errors++;
		errorPrinter.write(errorTxt);
		if (printError){
			System.out.println(errorTxt);
		}
	}
	
	
	public static void showMessage(String message){

		String msgTxt = "\n  -[INFO] "  +  "linea:columna (" + line + " , " + column + " ) :" + message;
		if (printMsg){
			System.out.println(msgTxt);
		}
	}
	public static void saveMessages(){
		
		errorPrinter.close();
		warningPrinter.close();
		
	}
}
