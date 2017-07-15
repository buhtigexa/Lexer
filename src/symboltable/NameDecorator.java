package symboltable;

public class NameDecorator {

	public 
	
	int nroAmbito;
	int nroAnidamiento;
	String decorado;
	
	
	public NameDecorator(){
        nroAmbito=0;
        nroAnidamiento=0;
        decorado="";

}
	
	public String decorate(String lexeme){

		String newName;
        decorado=lexeme;
        
        newName=lexeme + "_" +  Integer.toString(nroAmbito) + "_" + Integer.toString(nroAnidamiento);
        
        return newName;

}



	public String getDecorado(){
        return decorado;
}
}
