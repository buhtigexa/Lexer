import automaton.Lexer;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Lexer l = new Lexer("/home/marcelo/workspace/test_files/entrega1.txt");
		
		Object token=null;
		for (int i=0; i <200;i++){
			token=l.getToken();
			System.out.println();
			System.out.println("--" + token);
		}
		
		try
		{
			String t=(String)token;
			if (t.equals("T_EOF")){
				System.out.println(" >>>> END OF FILE <<<<");
			}
		}
		catch(ClassCastException e){
			System.out.println(" >>>> END OF FILE <<<<");
		}
		
		
		
		
	}
}

