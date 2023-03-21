package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public class Prettyprinting extends Visitor {

	@Override
	public void visit(BinaryComputing n) {
		n.getChild1().accept(this);
		System.out.print(" " + n.getOperation() + " ");
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

	}

	@Override
	public void visit(IfNode n) {
		System.out.print("hvis ");
		n.getExpr().accept(this);
		System.out.print(" :\n");
		for (AST child : n.getChildren()) {
			child.accept(this);
		}
		System.out.println(".\n");
	}

	@Override
	public void visit(LoopNode n) {
		System.out.println("gentag ");
		n.getChild1().accept(this);
		System.out.println(n.getRepeats() + " gange");
	}

	@Override
	public void visit(PrintNode n) {
		if(n.getId() != null) {
			System.out.println("print");
			n.getId().accept(this);
		} else if(n.getVal() != null) {
			System.out.println("print " + n.getVal());
		}
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
