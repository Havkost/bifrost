package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public abstract class Visitor {
	public void visit(AST n){
		//System.out.println ("In  AST visit\t"+n);
		n.accept(this);
	}

	abstract void visit(AssignNode n);
	abstract void visit(BinaryComputing n);
	abstract void visit(BoolskLiteral n);
	abstract void visit(DecimaltalLiteral n);
	abstract void visit(FuncDclNode n);
	abstract void visit(FuncNode n);
	abstract void visit(HeltalLiteral n);
	abstract void visit(IdNode n);
	abstract void visit(IfNode n);
	abstract void visit(LoopNode n);
	abstract void visit(PrintNode n);
	abstract void visit(ProgramNode n);
	abstract void visit(SymDeclaring n);
	abstract void visit(TekstLiteral n);
	abstract void visit(TypeNode n);
	abstract void visit(UnaryComputing n);
	abstract void visit(VarDclNode n);
	abstract void visit(TekstDcl n);
	abstract void visit(HeltalDcl n);
	abstract void visit(DecimalTalDcl n);
	abstract void visit(BoolskDcl n);
	//abstract void visit(SymReferencing n);
}
