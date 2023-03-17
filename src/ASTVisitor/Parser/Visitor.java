package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public abstract class Visitor {
	public void visit(AST n){
		//System.out.println ("In  AST visit\t"+n);

		n.accept(this);
	}

	abstract void visit(ProgramNode n);
	abstract void visit(SymDeclaring n);
	//abstract void visit(SymReferencing n);
	abstract void visit(FuncDclNode n);

	abstract void visit(FuncNode n);
	abstract void visit(IfNode n);

	abstract void visit(LoopNode n);

	abstract void visit(BinaryComputing n);
	
	abstract void visit(AssignNode n);

}
