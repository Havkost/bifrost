package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public abstract class Visitor {
	public abstract void visit(AssignNode n);
	public abstract void visit(BinaryComputing n);
	public abstract void visit(BoolskLiteral n);
	public abstract void visit(DecimaltalLiteral n);
	public abstract void visit(FuncDclNode n);
	public abstract void visit(FuncNode n);
	public abstract void visit(HeltalLiteral n);
	public abstract void visit(IdNode n);
	public abstract void visit(IfNode n);
	public abstract void visit(LoopNode n);
	public abstract void visit(PrintNode n);
	public abstract void visit(ProgramNode n);
	public abstract void visit(TekstLiteral n);
	public abstract void visit(UnaryComputing n);
	public abstract void visit(TekstDcl n);
	public abstract void visit(HeltalDcl n);
	public abstract void visit(DecimaltalDcl n);
	public abstract void visit(BoolskDcl n);
}
