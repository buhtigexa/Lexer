package generadorAssembler;

import java.util.Vector;

public class BancoRegistros {

	  Vector<Register> myRegisters;
	  
	  

	  public BancoRegistros(){
		  myRegisters  = new Vector<Register>();
	  }


public void addRegister(int pos,Register r){
        if ( myRegisters.size() > pos )
             myRegisters.setElementAt(r, pos);
}


public void addRegister(Register r){
        myRegisters.add(r);
}


public int  getPosicion(String r){

	 if ( (r.compareTo("ax")==0) ||   ( r.compareTo("eax")==0) )
	        return 3;
	 if ( (r.compareTo("dx")==0) ||   ( r.compareTo("edx")==0) )
	        return  2;
	 if ( (r.compareTo("cx")==0) ||   ( r.compareTo("ecx")==0) )
	        return 1;
	 if ( (r.compareTo("bx")==0) ||   ( r.compareTo("ebx")==0) )
	        return 0;
	 return -1;

}


public int  getPosicion(Register r){
        return this.getPosicion(r.getName());
}

public Register getRegister(Register r){
        return this.getRegister(r.getName());
}


public Register getRegister(int posReg){
        Register temp = null;
        if (  posReg < this.myRegisters.size() )
              temp = this.myRegisters.get(posReg);
        return temp;
}

public Register getRegister(String strRegname){
        Register temp = null;
        for ( int index = 0; index < this.myRegisters.size(); index++){
                temp =this.myRegisters.get(index);
                if ( strRegname.compareTo(temp.getName())==0)
                        return temp;
                }
        return temp;
}



public int isFree(){

        int maxReg = this.myRegisters.size();
        for ( int index = 0; index < maxReg; index++)
                if ( (myRegisters.get(index)).isFree() )
                      return ((int)index);
        return -1;
}


public boolean thisFree(){
        if ( this.isFree() >=0 )
                return true;
        return false;
}


public void setBusy(int pos){
        if ( pos < myRegisters.size() ){
                  (myRegisters.get(pos)).setBusy();
                  }
}


public void setBusy(Register r){
        int pos=this.getPosicion(r);
        (     this.myRegisters.get(  pos  )     ) .setBusy();
}


public void freeRegister(int pos){
        if ( pos < myRegisters.size() )
                  (myRegisters.get(pos)).freeRegister();
}



public void freeRegister(String reg){

        int pos = this.getPosicion(reg);
        if ((  pos >= 0 ) && (pos < (int)myRegisters.size() ) )
                (myRegisters.get(pos)).freeRegister();
}

public void freeRegister( Register r  ){
            this.freeRegister( r.getName() );
}



public Register getDifferent( String r){

        int pos = this.getPosicion(r);
        int free = this.isFree();
        Register temp = null;
        if ( ( free >= 0  ) && ( free != (int)pos ) ){
                temp = myRegisters.get(free);
                (this.myRegisters.get(free)).setBusy();
                return temp;          // devuelve el registro libre solicitado
                }

        else {          int maxReg = this.myRegisters.size();
                        for (  int index = 0; index < maxReg; index++)
                                if ( ( pos != index ) && ( (myRegisters.get(index)).isFree()) ) {
                                                temp = this.myRegisters.get(index);
                                                this.myRegisters.get(index).setBusy();
                                                return temp;
                               }                        // devuelve algun registro libre q no es el solicitado

                        if ( pos == 0 )  // Bx ==> Cx
                               temp = this.myRegisters.get(1);
                        if ( pos == 3 ) //  Ax ==> Bx
                             temp = this.myRegisters.get(0);
            }
       return temp;
}


public Register getDifferent( Register r ){

       return this.getDifferent(r.getName());
}



}
