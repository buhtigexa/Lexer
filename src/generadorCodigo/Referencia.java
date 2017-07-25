package generadorCodigo;

public class Referencia {
	
	public int nroTerceto;
	
	
	public Referencia(int referencia){
		
		this.nroTerceto=referencia;
	}
	
	public String toString(){
		
		String str="";
		str=" [ "  + nroTerceto +  " ] ";
		return str;
		
	}

}
