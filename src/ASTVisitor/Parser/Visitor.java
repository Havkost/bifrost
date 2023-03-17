package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public abstract class Visitor {

	public abstract void visit(ProgramNode n);
	public abstract void visit(SymDeclaring n);
	//abstract void visit(SymReferencing n);
	public abstract void visit(FuncDclNode n);

	public abstract void visit(FuncNode n);
	public abstract void visit(IfNode n);

	public abstract void visit(LoopNode n);

	public abstract void visit(BinaryComputing n);

	public abstract void visit(AssignNode n);

	public abstract void visit(UnaryComputing n);

	public abstract void visit(SymReferencing n);

}
