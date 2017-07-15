package codeGenerator;

import java.util.Vector;

public class RegisterBank {

	Vector<Register> myRegisters;


	RegisterBank(){
        this.myRegisters  = new vector<Register>;
}


void addRegister( int pos,Register r){
        if ( myRegisters.size() > pos )
             myRegisters.at(pos)=r;
}


void addRegister(Register r){
        myRegisters.push_back(r);
}


int  getPosicion(string r){

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


int  getPosicion(Register r){
        return this.getPosicion(r.getName());
}

Register getRegister(Register r){
        return this.getRegister(r.getName());
}


Register getRegister( int posReg){
        Register temp;
        if (  posReg < this.myRegisters.size() )
              temp = this.myRegisters.at(posReg);
        return temp;
}

Register getRegister(string strRegname){
        Register temp;
        for (  int index = 0; index < this.myRegisters.size(); index++){
                temp =this.myRegisters.at(index);
                if ( strRegname.compareTo(temp.getName())==0)
                        return temp;
                }
        return temp;
}



int isFree(){

         int maxReg = this.myRegisters.size();
        for (  int index = 0; index < maxReg; index++)
                if ( (myRegisters.at(index)).isFree() )
                      return ((int)index);
        return -1;
}


bool thisFree(){
        if ( this.isFree() >=0 )
                return true;
        return false;
}


void setBusy( int pos){
        if ( pos < myRegisters.size() ){
                  (myRegisters.at(pos)).setBusy();
                  }
}


void setBusy(Register r){
        int pos=this.getPosicion(r);
        (     this.myRegisters.at(  pos  )     ) .setBusy();
}


void freeRegister( int pos){
        if ( pos < myRegisters.size() )
                  (myRegisters.at(pos)).freeRegister();
}



void freeRegister(string reg){

        int pos = this.getPosicion(reg);
        if ((  pos >= 0 ) && (pos < (int)myRegisters.size() ) )
                (myRegisters.at(pos)).freeRegister();
}

void freeRegister( Register r  ){
            this.freeRegister( r.getName() );
}



Register getDifferent( string r ){

         int pos = this.getPosicion(r);
        int free = this.isFree();
        Register temp;
        if ( ( free >= 0  ) && ( free != (int)pos ) ){
                temp = myRegisters.at(free);
                (this.myRegisters.at(free)).setBusy();
                return temp;          // devuelve el registro libre solicitado
                }

        else {           int maxReg = this.myRegisters.size();
                        for (   int index = 0; index < maxReg; index++)
                                if ( ( pos != index ) && ( (myRegisters.at(index)).isFree()) ) {
                                                temp = this.myRegisters.at(index);
                                                this.myRegisters.at(index).setBusy();
                                                return temp;
                               }                        // devuelve algun registro libre q no es el solicitado

                        if ( pos == 0 )  // Bx ==> Cx
                               temp = this.myRegisters.at(1);
                        if ( pos == 3 ) //  Ax ==> Bx
                             temp = this.myRegisters.at(0);
            }
       return temp;
}


Register getDifferent( Register r ){

       return this.getDifferent(r.getName());
}






}
