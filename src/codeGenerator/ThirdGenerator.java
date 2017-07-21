package codeGenerator;

import java.util.Stack;
import java.util.Vector;

import automaton.Lexer;

public class ThirdGenerator {
	

public
		Stack<Operand> pila;
		Stack<Operand> operadores;
		Vector<Third> listaTercetos;
		Lexer lexer;
		int index;
		int noLabel;
//------------------------------------------------------
	public void syntaxError(String mensaje) {
		
        Lexer.showError(mensaje);
	}
	
	
	public ThirdGenerator(Lexer l){

        index=0;
        pila=new Stack<Operand>();
        operadores=new Stack<Operand>();
        lexer=l;
        listaTercetos = new Vector<Third>();
        noLabel=0;

}
	public void apilarTerceto(Operand  t){
        pila.push(t);
}
	public Operand  desapilar(){

        Operand  temp= new Operand(-1);
        if (!(pila).empty()) {
                temp = pila.peek();
                pila.pop();
        }
        return temp;
}


	public void completarSalto(boolean plus){
         int NoTerceto=0;
         int incremento = 0;
         if ( plus )
                incremento = 1;
        if (!operadores.empty()){
                Operand  nroTerceto = new Operand( (operadores.peek()).getPosicion() );
                operadores.pop();
                   if ( nroTerceto.isSalto() )
                                NoTerceto = nroTerceto.getPosicion();
                        else
                                {
                                String mensaje="no se puede completar el salto.";
                                Lexer.showError(mensaje);
                                }
                 int salto = (listaTercetos.size())+ incremento;
                Operand  operandoSalto = new Operand(salto);


                listaTercetos.get(NoTerceto).setLeftOp(operandoSalto);
                }

}

public void completarSaltoDo(boolean plus){

          if (!operadores.empty()){
                Operand  salto = new Operand((operadores.peek()).getPosicion());
                operadores.pop();
                listaTercetos.get(listaTercetos.size()-1).setLeftOp(salto);
              }

}


public void showTercetos(){

        String NoTerceto;

        //Form1.Memo5="";
        String left;
        String right;
        left="";
        right="";
          for (  int i = 0; i < listaTercetos.size();i++){
             NoTerceto = (Integer.toString(listaTercetos.get(i).getId() ));
             if ( (listaTercetos.get(i) != null ) && (listaTercetos.get(i).getLeftOp().isSalto() ))
                   left = "[" + ((listaTercetos.get(i)).getLeftOp()).getValue() + "]";
             else  left =(listaTercetos.get(i)).getLeftOp().getValue();
             if ( (listaTercetos.get(i) != null ) && ( (listaTercetos.get(i)).getRightOp().isSalto() ))
                   right = "[" + (listaTercetos.get(i)).getRightOp().getValue() + "]";
             else  right =(listaTercetos.get(i)).getRightOp().getValue();

             System.out.println(NoTerceto + "-( " + (listaTercetos.get(i)).getOperator() + " , " +  left + " , " + right + "):" + listaTercetos.get(i).getType() );
          }
       }


	public String toString(){

		String str="";
		String NoTerceto="";
		String left;
        String right;
        left="";
        right="";
          for (  int i = 0; i < listaTercetos.size();i++){
             NoTerceto = (Integer.toString(listaTercetos.get(i).getId() ));
             if ( (listaTercetos.get(i) != null ) && (listaTercetos.get(i).getLeftOp().isSalto() ))
                   left = "[" + ((listaTercetos.get(i)).getLeftOp()).getValue() + "]";
             else  left =(listaTercetos.get(i)).getLeftOp().getValue();
             if ( (listaTercetos.get(i) != null ) && ( (listaTercetos.get(i)).getRightOp().isSalto() ))
                   right = "[" + (listaTercetos.get(i)).getRightOp().getValue() + "]";
             else  right =(listaTercetos.get(i)).getRightOp().getValue();

            str+="\n  " + NoTerceto + "-( " + (listaTercetos.get(i)).getOperator() + " , " +  left + " , " + right + "):" + listaTercetos.get(i).getType();
          }
          
          return str;

		
	}

	public Stack<Operand  > getPila(){
        return pila;
	}

	public Stack<Operand  > getOperadores(){
        return operadores;
	}
public void showPila(Stack<Operand > p){

      Stack<Operand> aux=new Stack<Operand>();;
      String NoTerceto;
      if ( p!=null) {
        while ( !p.empty() ) {
                 Operand  op = new Operand( p.peek() );
                 p.pop();
                 aux.push(op);
               }
          }

      if ( aux !=null)
          while ( !aux.empty()){
                 Operand  op= new Operand(aux.peek());
                 aux.pop();
                 p.push(op);
            }

      if ( aux!=null)
                aux=null;

}
void showOperadores(){



}

public void apilarOperando(Operand  op){

        if ( op!=null){
                        operadores.push(op);
        }
}


 public int countTercetos(){
        return (listaTercetos.size());
}



void apilarOperador(Operand  op){
      apilarOperando(op);
      showPila(operadores);
}

public void desapilarOperando(){

        if ( !operadores.empty() )
                operadores.pop();
       }

void desapilarOperador(){
        desapilarOperando();


}


public void generarTerceto(Third t){
        //listaTercetos.get(index)=t;
    listaTercetos.set(index, t);    
	index=index+1;

}

public void generarTerceto(String op, boolean apilar){
 /********************************************
 Tercetos: BI,BF,PRINT,=,<,>,>=,<=,+,-,*,/,:=,tolong
  **********************************************/
       String opBF="BF";
       String opBI="BI";
       String opPrint="print";
       String opTolong="tolong";
       String opMul="*";
       String opLType;
       String opRType;
       String tercetoType;
       boolean isOpR, isOpL;

       isOpR=false;
       isOpL=false;

       opLType="";
       opRType="";
       tercetoType="";

       Operand  opL=new Operand();
       Operand  opR=new Operand();


       if ( ( !(op.compareTo(opBF)== 0) && !(op.compareTo(opBI)==0)  ) )
             if ( !(operadores.empty()) ){
                        opR = new Operand(operadores.peek() );
                        opRType=getType(opR);
                        isOpR=true;
                        desapilarOperando();
                        if ( !( op.compareTo(opTolong)==0)  && !( op.compareTo(opPrint)==0 ) ) {
                                if ( !(operadores.empty()) ){
                                                opL= new Operand(operadores.peek() );
                                                opLType=getType(opL);
                                                desapilarOperando();
                                                     isOpL=true;
                                       }
                         }
                      }


      Third t = new Third(op,opL,opR);



      if  ( op.compareTo(opTolong)==0)   {
              tercetoType="long";
             }

      if ( (isOpR) && (isOpL) )
           if (  opRType.compareTo(opLType) != 0 ){
        	   			
        	   			System.out.println("------------------------------------------------------------");
        	   			System.out.println("("  + op.toString()  + " , " + opL.getValue() + "[" + opL.getType() + "]" + "  , "  + opR.getValue() +"[" + opR.getType() +"]   Type :" +  t.getType() +") ");
                        String mensaje="Tipos de datos incomparables: Se requiere conversión explícita.";
                        Lexer.showError(mensaje);
                        tercetoType="operands don't match";
                        }
              else
                   tercetoType=opRType;
       t.setType(tercetoType);
       t.setId(listaTercetos.size());

       if ( apilar )
           operadores.push( new Operand(listaTercetos.size()) );

       listaTercetos.addElement(t);
       index=index+1;



}

//------------------------------------------------------

public String getType(Operand op){
  String type;
  type="";

             if ( op != null ) {
                        if ( op.isSalto() ){
                                 int posicion = op.getPosicion();
                                if ( ( posicion < listaTercetos.size() ) && (listaTercetos.get(posicion)!=null) )
                                         type=(listaTercetos.get(posicion)).getType();

                               }
                        else
                                        type=op.getType();
                      }

   return type;
}
//------------------------------------------------------
public Vector<Third> getTercetos(){

           return (listaTercetos);
}
//------------------------------------------------------
public Third getTerceto(int pos){
        Third temp=null;
        if ( pos < ((int)listaTercetos.size()) )
               temp = new Third( listaTercetos.get(pos) );
        return temp;
}
//------------------------------------------------------
public String getNewLabel(int index){

        String noJmp=(Integer.toString(index));
        String temp = "label"+noJmp;
        return temp;


}
//------------------------------------------------------
public void labeler(String finalLabel){
        Vector<Third> myTercetos = getTercetos();
        Third src;
        Third dest = null;
        Third prev = null;
        Operand opDest;
        String label;
         int posDest;
        int posPrev;
        String operatorTPrev;
         int nTercetos=myTercetos.size();

        if ( myTercetos != null ){
                nTercetos=myTercetos.size();
                for (  int index=0;index < nTercetos; index++)  {
                        src =  myTercetos.get(index);
                        posPrev = index-1;
                        if  (src != null ){
                                if ( ( posPrev >= 0 ) && (( int) posPrev < nTercetos) ){
                                                        prev = myTercetos.get(posPrev);
                                                        operatorTPrev = prev.getOperator();
                                                        src.setOpPrevious(operatorTPrev);

                                                      }
                                String i = Integer.toString(posPrev);
                                if ( ( src.getOperator().compareTo("BF")==0 ) ||  (  src.getOperator().compareTo("BI")==0 ) ){
                                       posDest=(src.getLeftOp()).getPosicion();
                                       String noT=Integer.toString(index);
                                       if ( src.getOperator().compareTo("BF")==0){
                                                src.setType(prev.getType());
                                               }
                                       else     { 
                                    	   src.setType(dest.getType());
                                       }

                                       if ( posDest < nTercetos ){
                                            dest=myTercetos.get(posDest);
                                            label=getNewLabel(posDest); (src).setLabelSrc(label); dest.setLabelDst(label);
                                         }
                                        else   { src.setLabelSrc(finalLabel);
                                              }

                               }
                        }
                 }
        }
}
	
}