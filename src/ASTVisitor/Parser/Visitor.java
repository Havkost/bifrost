package ASTVisitor.Parser;

public abstract class Visitor {
	public void visit(Node n){
		//System.out.println ("In  AST visit\t"+n);

		n.accept(this);
	}

	abstract void visit(Prog n);
	abstract void visit(SymDeclaring n);
	//abstract void visit(SymReferencing n);

	abstract void visit(Computing n);
	
	abstract void visit(Assigning n);

}
