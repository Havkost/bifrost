package ASTVisitor;

public abstract class Visitor {
	public void visit(AST n){ 
		//System.out.println ("In  AST visit\t"+n);

		n.accept(this);
	}

	abstract void visit(Prog n);
	abstract void visit(SymDeclaring n);
	abstract void visit(SymReferencing n);
	


}
