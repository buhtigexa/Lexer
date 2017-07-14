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
	Register registerDestiny;
	
	public Third(){
	
		leftOp=new Operand();
		rightOp=new Operand();
		operator=new String();
		id=0;
	}

	public Third(Third t){
		
		this.operator=t.operator;
		this.leftOp=new Operand(t.leftOp);
		this.rightOp=new Operand(t.rightOp);
		this.type=t.getType();
		this.labelSrc=t.getLabelSrc();
		this.labelDst=t.getLabelDst();
		this.id=t.getId();
		this.opPrevious=t.getOpPrevious();
	}
	
	
	public Third(String oper,Operand opLeft,Operand opRight){
	 
		this.operator=oper;
		this.leftOp=new Operand(opLeft);
		this.rightOp=new Operand(opRight);
	}
	
	public boolean checkType(Operand op1,Operand op2){
		
		if (op1.getType().equals(op2.getType())){
			return false;
		}
		return true;
		
	}

	
	
	public Operand getLeftOp() {
		return leftOp;
	}

	public void setLeftOp(Operand leftOp) {
		this.leftOp = leftOp;
	}

	public Operand getRightOp() {
		return rightOp;
	}

	public void setRightOp(Operand rightOp) {
		this.rightOp = rightOp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabelSrc() {
		return labelSrc;
	}

	public void setLabelSrc(String labelSrc) {
		this.labelSrc = labelSrc;
	}

	public String getLabelDst() {
		return labelDst;
	}

	public void setLabelDst(String labelDst) {
		this.labelDst = labelDst;
	}

	public String getOpPrevious() {
		return opPrevious;
	}

	public void setOpPrevious(String opPrevious) {
		this.opPrevious = opPrevious;
	}

	public Register getRegisterDestiny() {
		return registerDestiny;
	}

	public void setRegisterDestiny(Register registerDestiny) {
		this.registerDestiny = registerDestiny;
	}

	public Register getRegister(){
		
	    Register temp;
        temp = registerDestiny;
        return temp;
        
	} 

}

	




