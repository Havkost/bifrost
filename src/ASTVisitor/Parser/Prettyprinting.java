package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public class Prettyprinting extends Visitor {

	@Override
	public void visit(BinaryComputing n) {
		n.getChild1().accept(this);
		System.out.print(" " + n.getOperation().toLowerCase() + " ");
		n.getChild2().accept(this);
	}

	@Override
	public void visit(BoolskLiteral n) {
		System.out.print(n.getVal());
	}

	@Override
	public void visit(DecimaltalLiteral n) {
		System.out.print(n.getVal());
	}

	@Override
	public void visit(TekstLiteral n) {
		System.out.print(n.getVal());
	}

	@Override
	public void visit(HeltalLiteral n) {
		System.out.print(n.getVal());
	}

	@Override
	public void visit(ProgramNode n) {
		for(AST ast : n.getChild()){
			ast.accept(this);
			System.out.println();
		}
	}

	@Override
	public void visit(SymDeclaring n) {
	}

	@Override
	public void visit(TypeNode n) {

	}

	@Override
	public void visit(FuncDclNode n) {

	}

	@Override
	public void visit(FuncNode n) {

	}

	@Override
	public void visit(IdNode n) {
		System.out.print(n.getName());
	}

	@Override
	public void visit(IfNode n) {
		System.out.print("hvis ");
		n.getExpr().accept(this);
		System.out.print(":\n");
		for (AST child : n.getChildren()) {
			System.out.print("	");
			child.accept(this);
		}
		System.out.print(".");
	}

	@Override
	public void visit(LoopNode n) {
		System.out.print("gentag ");
		n.getChild1().accept(this);
		System.out.print(n.getRepeats() + " gange");
	}

	@Override
	public void visit(PrintNode n) {
		System.out.print("print ");
		n.getValue().accept(this);
	}


	@Override
	public void visit(AssignNode n) {
		System.out.print("sæt " + n.getId() + " til ");
		n.getVal().accept(this);
	}

	@Override
	public void visit(UnaryComputing n) {
	}

	@Override
	public void visit(TekstDcl n) {
		System.out.print("gem tekst ");
		n.getValue().accept(this);
		System.out.print(" som " + n.getId());
	}

	@Override
	public void visit(HeltalDcl n) {
		System.out.print("gem heltal ");
		n.getValue().accept(this);
		System.out.print(" som " + n.getId());
	}

	@Override
	public void visit(DecimaltalDcl n) {
		System.out.print("gem decimaltal ");
		n.getValue().accept(this);
		System.out.print(" som " + n.getId());
	}

	@Override
	public void visit(BoolskDcl n) {
		System.out.print("gem boolsk ");
		n.getValue().accept(this);
		System.out.print(" som " + n.getId());
	}

	@Override
	public void visit(SymReferencing n) {
	}
}
