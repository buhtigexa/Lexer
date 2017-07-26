package generadorCodigo;

public class Referencia extends Operando{
	
	public int nroTerceto;
	public static GeneradorTercetos generadorTercetos;
	
	public Referencia(int referencia){
		
		this.nroTerceto=referencia;
		isReferencia=true;
	}
	
	public String toString(){
		
		String str="";
		str=" [ "  + nroTerceto +  " ] ";
		return str;
	
	}
	
	protected Terceto getTerceto(){
		
		Terceto terceto=generadorTercetos.getTerceto(nroTerceto);
		
		return terceto;
		
	}
	
	public String getType(){
		
		return getTerceto().getType();
	}

	public boolean isAsignacion(){
		
		Terceto t = getTerceto();
		return (t.operador.compareTo(":=")==0);
	}
}
