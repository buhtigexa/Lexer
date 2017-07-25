package generadorCodigo;

public class Terceto extends Operando {

	public Object opD;
	public Object opI;
	public String operador;
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
		this.operador=operador;
		this.opD=opD;
		this.opI=opI;
		this.type=new String();
		
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
		
		str= "[ (" + operador  + ") "+ opIstr.toString()  + "   ---   "  + opDstr.toString()  + " --type : <" + type +">]";
		
		//str="[ (" + operador  + ") "+ opI  + "   ---   "  + opD + " --type : <" + type +">]";
		return str;
	}
	
	
}
