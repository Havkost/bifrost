package acASTVisitor;

public abstract class Visitor {
	public void visit(AST n){ 
		//System.out.println ("In  AST visit\t"+n);

		n.accept(this);
	}
	
	abstract void visit(Assigning n);
	abstract void visit(Computing n);
	abstract void visit(ConvertingToFloat n);
	abstract void visit(FloatConsting n);
	abstract void visit(IntConsting n);
	abstract void visit(Printing n);
	abstract void visit(Prog n);
	abstract void visit(SymDeclaring n);
	abstract void visit(FloatDcl n);
	abstract void visit(IntDcl n);
	abstract void visit(SymReferencing n);
	


}
