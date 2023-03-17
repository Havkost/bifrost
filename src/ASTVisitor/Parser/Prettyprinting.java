package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public class Prettyprinting extends Visitor {

	@Override
	public void visit(BinaryComputing n) {
		// TODO Auto-generated method stub
		n.getChild1().accept(this);
		System.out.print(" " + n.getOperation() + " ");
		n.getChild2().accept(this);

	}

	@Override
	void visit(BoolskLiteral n) {

	}

	@Override
	void visit(DecimaltalLiteral n) {

	}

	@Override
	public void visit(ProgramNode n) {
		// TODO Auto-generated method stub

		for(AST ast : n.getChild()){
			ast.accept(this);
		};
		System.out.println();

	}

	@Override
	public void visit(SymDeclaring n) {
		// TODO Auto-generated method stub

	}

	@Override
	void visit(TekstLiteral n) {

	}

	@Override
	void visit(TypeNode n) {

	}

	@Override
	public void visit(FuncDclNode n) {

	}

	@Override
	public void visit(FuncNode n) {

	}

	@Override
	void visit(HeltalLiteral n) {

	}

	@Override
	void visit(IdNode n) {

	}

	@Override
	public void visit(IfNode n) {

	}

	@Override
	public void visit(LoopNode n) {

	}

	@Override
	void visit(PrintNode n) {

	}

	@Override
	public void visit(AssignNode n) {

	}

	@Override
	public void visit(UnaryComputing n) {

	}

	@Override
	void visit(VarDclNode n) {

	}
}
