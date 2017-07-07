package automaton;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Hashtable;

import semanticActions.Accumulate;
import semanticActions.Clear;
import semanticActions.Constant;
import semanticActions.Empty;
import semanticActions.ID;
import semanticActions.Rewind;
import semanticActions.SemanticAction;
import semanticActions.SequentialAction;
import semanticActions.Token;
import semanticActions.WhiteChar;

public class Lexer {

	final static int INVALID_CHAR = 20;
	protected String filePath;
	protected RandomAccessFile raf;
	public String lexeme;
	public final static int MaxState=16;
	
	long position;
	
	public int line,column,errors,warnings;
	
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
	
	
	// composite semantic actions 
	
	public SemanticAction as12 = new SequentialAction(this, as1, as2);

	public SemanticAction as34 = new SequentialAction(this, as3, as4);
			
	public SemanticAction as36 = new SequentialAction(this, as3, as6);
	
	public SemanticAction as37 = new SequentialAction(this, as3, as7);
	
	public SemanticAction as38 = new SequentialAction(this, as3, as8);
	
	
	public SemanticAction as13 = new SequentialAction(this,as1,as3);
	
	public Hashtable<String, String> reservedWords;

	
	public int automaton[][]={
																							// invalid char
	/*		0   1   2   3    4   5   6   7   8   9  10  11  12  13  14  15  16  17  18  19	20*/
	
	/*e0*/ {e2, e1,e12,e10, e3, e3, e4, ef, ef, e3, e3, e8, e3, e0, e0, e0, e0, e3, e3, e3, ef},
	
	/*e1*/ {ef, e1, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e2*/ {e2, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e3*/ {ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e4*/ {ef, ef, ef, ef, ef, ef, ef, e5, e5, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e5*/ {ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e6*/ {ef, ef, ef, ef, ef, ef, ef, e7, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e7*/ {ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e8*/ {ef, ef, ef, ef, ef, ef, ef, e9, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e9*/ {ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e10*/{ef, ef, ef, e11,ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e11*/{ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e12*/{ef, ef, ef, ef, ef, ef, ef, ef, ef, e13,ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e13*/{e13,e13,e13,e13,e13,e13,e13,e13,e13,e13,e14,e13,e13,e13,e13,e13,e13,e13,e13,e13,ef},
	
	/*e14*/{e13,e13,e15,e13,e13,e13,e13,e13,e13,e13,e14,e13,e13,e13,e13,e13,e13,e13,e13,e13,ef},
	
	/*e15*/{ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef},
	
	/*e16*/{ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef, ef}
	
	};
	
	
	public SemanticAction actions[][]={

	/*		0     1      2     3     4     5     6     7     8      9   10    11    12    13    14    15   16     17    18    19   20   */
			
	/*e0*/ {as12,as12, as12, as12, as12, as12, as12,  as0, as12, as12, as12,  as12, as12, as5,  as5,  as5,  as5,  as12,  as12,as12,  as0},
	
	/*e1*/ {as34,as2 , as34, as34, as34, as34, as34, as34, as34, as34, as34, as34, as34, as34, as34, as34, as34, as34, as34, as34,  as0},
	
	/*e2*/ {as2 ,as36, as36, as36, as36, as36, as36, as36, as36, as36, as36, as36, as36, as36, as36, as36, as36, as36, as36, as36,  as0},
	
	/*e3*/ {as37,as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37,  as0},
	
	/*e4*/ {as37,as37, as37, as37, as37, as37, as37,  as2,  as2, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37,  as0},
	
	/*e5*/ {as37,as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37,  as0},
	
	/*e6*/ {as37,as37, as37, as37, as37, as37, as37,  as2, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37,  as0},
	
	/*e7*/ {as37,as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37,  as0},
	
	/*e8*/ {as38,as38, as38, as38, as38, as38, as38,  as2, as38, as38, as38, as38, as38, as38, as38, as38, as38, as38, as38, as38,  as0},
	
	/*e9*/ {as37,as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37,  as0},
	
	/*e10*/{as37,as37, as37,  as2, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37,  as0},
	
	/*e11*/{as37,as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37, as37,  as0},
	
	/*e12*/{as37,as37, as37, as37, as37, as37, as37, as37, as37, as0, as37, as37, as37, as37, as37, as37, as37, as37, as37,  as37,  as0},
	
	/*e13*/{as0,  as0,  as0, as0,  as0,  as0,  as0,   as0,  as0, as0,  as0,  as0,  as0,  as0, as5 ,  as5,  as5,  as0,  as0,   as0,  as0},
	
	/*e14*/{ as3, as3,  as0,  as3,  as3,  as3,  as3,  as3,  as3,  as3, as3,  as3,  as3,  as3,  as3 ,  as3,  as3,  as3,  as3,  as3,  as0},
	
	/*e15*/{as13,as13, as13, as13 ,as13, as13, as13, as13, as13, as13, as13, as13, as13, as13, as13,  as13, as13, as13, as13, as13, as0},
	
	/*e16*/{as0,  as0,  as0,  as0 ,as0,   as0,  as0,  as0,  as0,  as0,  as0,  as0,  as0,  as0,  as0,   as0, as0,  as0,  as0,  as0,  as0}
	};
	
	public Lexer(String path){
		setUp();
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
		
		return map;
		
	}
	
	public Object getToken(){
		
		Object token=null;
		int e_i=e0;
		int code=0;
		char c=0;
		SemanticAction currentAction=null;
		
		try{
			
			while ( e_i != ef ){
				
				c=(char)raf.readByte();
				position++;
				code=map(c);
				currentAction=actions[e_i][code];
				e_i=automaton[e_i][code];
				token=currentAction.execute(c);
				if (map(c)==INVALID_CHAR){
					errors++;
					System.out.println("[Error: Caracter inválido- linea,columna] : " + line + " , "  + column + "  " + c );
				}
				
				if ((e_i==ef) && (token==null)){
					e_i=e0;
				}
			}
			
		}
		
		catch(EOFException e){
		
			//System.out.println("EOF");
			token="T_EOF";
		
		}
		catch(IOException e){
			
		}
		
		return token;
		
	}
	

	public void rewind(){
		
		position--;
		column--;
		try {
			raf.seek(position);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected void setUp(){
		
		reservedWords=new Hashtable<String, String>();
		reservedWords.put("long","long");
		reservedWords.put("uint","uint");
		reservedWords.put("if","if");
		reservedWords.put("then","then");
		reservedWords.put("else","else");
		reservedWords.put("do","do");
		reservedWords.put("while","while");
		reservedWords.put("print","print");
		reservedWords.put("tolong","tolong");
	
		position=0;
	
	}
	
	
	
	
}
