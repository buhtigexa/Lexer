package codeGenerator;

public class Third {

	Operand leftOp;
	Operand rightOp;
	int id;
	String operator;
	String type;
	String labelSrc;
	String labelDst;
	String opPrevious;
	Register register;
	
	public Third(){
	
		leftOp=new Operand();
		rightOp=new Operand();
		operator=new String();
		id=0;
		
		
	}

	public boolean checkType(Operand op1,Operand op2){
		
		if (op1.getType()==(op2.getType())){
			return false;
		}
		return true;
		
	}

	/*
	
	Register Terceto::getRegister(){
        Register temp;
        temp = this->regDestiny;
        return temp;
*/

}

	




