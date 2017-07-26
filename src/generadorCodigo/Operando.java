package generadorCodigo;


public abstract class Operando {
	
	
	public boolean isReferencia;
	
	public  boolean isReferencia() {
		return isReferencia;
	}

	public Operando(){
		
		isReferencia=false;
		
	}
	
	public abstract String toString();

	public abstract String getType();
		

}
