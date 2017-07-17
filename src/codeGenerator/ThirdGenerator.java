package codeGenerator;

import java.util.Stack;
import java.util.Vector;

import automaton.Lexer;

public class ThirdGenerator {
	

	protected
		
		Stack<Operand> operators;
		Vector<Third> thirdList;
		Stack<Operand> stack;
		Lexer lexer;
		int index;
		int noLabel;

		
	    //public String getNewLabel(int dex){
		//	return new String();
		//}
		
		

	    public ThirdGenerator(Lexer l){

	        index=0;
	        stack=new Stack<Operand>();
	        operators=new Stack<Operand>();
	        lexer=l;
	        thirdList = new Vector<Third>();
	        noLabel=0;

	       }

	     public void stackThird(Operand t){
	    	   stack.push(t);
	    	 
	     }
	    
	    public Operand desapilar(){

        Operand temp= new Operand(-1);
        if  (!stack.isEmpty() ) {
                temp = stack.peek();// equals to top() method
                stack.pop();
        }
        return temp;
}


	    public void completarSalto(boolean plus){
        
	    	int NoTerceto=0;
	    	int incremento = 0;
	    	if ( plus )
                incremento = 1;
	    	
	    	if ( operators.isEmpty()  ){
                Operand nroTerceto = new Operand( (operators.peek() ).getPosicion() );
                operators.pop();
                   if ( nroTerceto.isSalto() )
                        NoTerceto = nroTerceto.getPosicion();
                     else
                          {
                           //TForm1::writer("[Error]" + Lexer->getNroLinea() +"  ... no se puede completar el salto.");
                    	 	System.out.println("[Error]" + lexer.getLine() +"  ... no se puede completar el salto.");
                          }
                int salto = (thirdList.size())+ incremento;
                Operand operandoSalto = new Operand(salto);
                thirdList.get(NoTerceto).setLeftOp(operandoSalto);
                }

	    }

	    public void completarSaltoDo(boolean plus){

          if (!operators.isEmpty()){
                Operand salto = new Operand((operators.peek()).getPosicion());
                operators.pop();
                //listaTercetos->at(listaTercetos->size()-1)->setLeft(salto);
                thirdList.get(thirdList.size()-1).setLeftOp(salto);
          }
	    }
	    
	    public void showTercetos(){

	    	String NoTerceto=new String();
	    	//Form1->Memo5->Clear();
	        String left;
	        String right;
	        left="";
	        right="";
	         for ( int i = 0; i < thirdList.size();i++){
	             NoTerceto = Integer.toString(thirdList.get(i).id);
	           	 if ( (thirdList.get(i) != null ) && (thirdList.get(i).getLeftOp().isSalto()))
	            	 left="["+thirdList.get(i).getLeftOp().getValue()+"]";
	           	 else left=(thirdList.get(i).getLeftOp().getValue());
	           	 
	             if ((thirdList.get(i)!=null) && (thirdList.get(i).getRightOp().isSalto()))
	            	 right="["+thirdList.get(i).getRightOp().getValue()+"]";
	           	 else 
	           		 right=(thirdList.get(i).getLeftOp().getValue());
	             System.out.println(NoTerceto + "-( " + (thirdList.get(i).getOperator() + " , " +  left + " , " + right + ")" ));
	             
	         }
	       }


	    public Stack<Operand> getPila(){
	    	return stack;
	    }

	    public Stack<Operand> getOperadores(){
	    	return operators;
	    }
	    public void showPila(Stack<Operand> p){

		      Stack<Operand> aux=new Stack<Operand>();
		      String NoTerceto;
		      if ( p!=null) {
		        while ( !p.isEmpty() ) {
		                 Operand op = new Operand( p.peek() );
		                 p.pop();
		                 aux.push(op);
		               }
		          }
		
		      if ( aux !=null)
		          while ( !aux.isEmpty()){
		                 Operand op= new Operand(aux.peek());
		                 aux.pop();
		                 p.push(op);
		            }
		
		      if ( aux!=null){
		                aux=null;
		      }
		}
	    public void showOperadores(){
	    
	    }

	    public void apilarOperando(Operand op){
	    	if ( op!=null){
                 operators.push(op);
	    		}
	    }

	    public int countTercetos(){
	    	return (thirdList.size());
	    }

	    public void apilarOperador(Operand op){
	    		apilarOperando(op);
	    		showPila(operators);
	    }

	    public void desapilarOperando(){

	    	if ( !operators.isEmpty() )
	    			operators.pop();
	    	}

	    public void desapilarOperador(){
	    	desapilarOperando();
	    }

	    public void generarTerceto(Third t){
	    	//listaTercetos->at(index)=t;
	    	thirdList.add(index, t);
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

       Operand opL=new Operand();
       Operand opR=new Operand();


       if ( ( !(op.compareTo(opBF)== 0) && !(op.compareTo(opBI)==0)  ) )
             if ( !operators.isEmpty() ) {
                        opR = new Operand(operators.peek());
                        opRType=getType(opR);
                        isOpR=true;
                        desapilarOperando();
                        if ( !( op.compareTo(opTolong)==0)  && !( op.compareTo(opPrint)==0 ) ) {
                                if ( !(operators.isEmpty()) ){
                                                opL= new Operand(operators.peek() );
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
                         syntaxError("Tipos de datos incomparables: Se requiere conversión explícita.");
                         tercetoType="operands don't match";
                        }
              else
                   tercetoType=opRType;
       t.setType(tercetoType);
       t.setId(thirdList.size());

       if ( apilar )
           operators.push( new Operand(thirdList.size()) );

       thirdList.add(t);
       this.index=this.index+1;



}
//------------------------------------------------------


public String getType(Operand op){
  String type;
  type="";
  if ( op != null ) {
       if ( op.isSalto() ){
    	   int posicion = op.getPosicion();
           if ( ( posicion < thirdList.size()) && ( thirdList.get(posicion) )!=null) 
        	   type=(thirdList.get(posicion)).getType();
       		}
            else
            	type=op.getType();
        }

   return type;
}
//------------------------------------------------------
	public Vector<Third> getTercetos(){
           return thirdList;
	}
//------------------------------------------------------
public Third getTerceto(int pos){
        Third temp=null;
        if ( pos < ((int)thirdList.size()) )
        	temp = new Third( thirdList.get(pos) );
        return temp;
}
//------------------------------------------------------
public String getNewLabel(int index){

        //String noJmp=(IntToStr(index)).c_str();
        String noJmp=Integer.toString(index);
		String temp = "label"+noJmp;
        return temp;


}
//------------------------------------------------------
public void labeler(String finalLabel){
        
		Vector<Third> myTercetos = getTercetos();
        Third src=new Third();
        Third dest=new Third();
        Third prev=new Third();
        Operand opDest=new Operand();
        String label;
        int posDest;
        int posPrev;
        String operatorTPrev;
        int nTercetos=myTercetos.size();

        if ( myTercetos != null ){
                nTercetos=myTercetos.size();
                for ( int index=0;index < nTercetos; index++)  {
                        //src =  myTercetos->at(index);
                        src = myTercetos.get(index);
                        posPrev = index-1;
                        if  (src != null){
                                if ( ( posPrev >= 0 ) && ((int) posPrev < nTercetos) ){
                                	prev = myTercetos.get(posPrev);
                                	operatorTPrev = prev.getOperator();
                                    src.setOpPrevious(operatorTPrev);
                                }
                                String i = Integer.toString(posPrev);
                                if ( ( src.getOperator().compareTo("BF")==0 ) ||  (  src.getOperator().compareTo("BI")==0 ) ){
                                       posDest=(src.getLeftOp()).getPosicion();
                                       String noT = Integer.toString(index);
                                       if ( src.getOperator().compareTo("BF") == 0 ){
                                                src.setType(prev.getType());
                                               }
                                       else     { src.setType(dest.getType());}

                                       if ( posDest < nTercetos ){
                                            dest=myTercetos.get(posDest);
                                            label=getNewLabel(posDest); src.setLabelSrc(label); dest.setLabelDst(label);
                                         }
                                        else   { src.setLabelSrc(finalLabel);
                                              }

                               }
                        }
                 }
        }
}
//------------------------------------------------------
	public void syntaxError(String mensaje) {
		//TForm1::writer("[Error] Línea " + Lexer->getNroLinea() + ": " + mensaje + ".");
        System.out.println("[Error] Línea " +lexer.getLine()+ ": " + mensaje + ".");
		//TForm1::incErrors();
	}
}