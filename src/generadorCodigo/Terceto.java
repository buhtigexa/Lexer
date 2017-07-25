package generadorCodigo;

public class Terceto {

	public Object opD;
	public Object opI;
	public String operador;
	public boolean isAsignacion;
	
	public String getOperador() {
		return operador;
	}


	public int id;
	public String type;
	
	public void setType(String type) {
		this.type = type;
	}


	public String getType() {
		return type;
	}


	public Storage storage;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	
	
	public Terceto(String operador,Object opI, Object opD){
		
		super();
		setUp(operador, opI, opD);
		
	}
		
	public void setUp(String operador,Object opI,Object opD){
		

		this.operador=operador;
		this.opD=opD;
		this.opI=opI;
		this.type=new String();
		this.isAsignacion=isAsignacion();
		
	}
	
	public boolean isAsignacion(){
		
		if (operador.compareTo(":=")==0){
			this.isAsignacion=true;
		}
		return isAsignacion;
	}
	
	public String toString(){
		
		
		  String str="";
		 
		
		Object opIstr=null;
		Object opDstr=null;
		
		
		//if (opI!=null){
			
			opIstr="["+opI+"]";
		//}
			
		//if (opD !=null){
		
			opDstr="["+opD+"]";
		//}
		
		if ( opI instanceof Terceto){
			opIstr=new Referencia( ((Terceto)opI).getId() );
		}
		
		if ( opD instanceof Terceto){
			opDstr=new Referencia( ((Terceto)opD).getId() );
		}
		
		str= id +" [ (" + operador  + ") "+ opIstr.toString()  + "   ---   "  + opDstr.toString()  + " --type : <" + type +">]";
		
		//str="[ (" + operador  + ") "+ opI  + "   ---   "  + opD + " --type : <" + type +">]";
		return str;
	}
	
	
}
