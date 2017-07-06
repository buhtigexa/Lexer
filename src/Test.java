import automaton.Lexer;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Lexer l = new Lexer("/home/marcelo/workspace/test_files/entrega1.txt");
		
		Object token=null;
		while (token==null){
			token=l.getToken();
		}
		
		String t=(String)token;
		if (t.equals("T_EOF")){
			System.out.println(" >>>> END OF FILE <<<<");
		}
		
		/*
		l = new Lexer("/home/marcelo/workspace/test_files/tp2-ambitos-32-ok.txt");
		
		token=null;
		while (token==null){
			token=l.getToken();
		}
		
		t=(String)token;
		if (t.equals("T_EOF")){
			System.out.println(" >>>> END OF FILE <<<<");
		}
		
		*/
		
		
		
	}
}

