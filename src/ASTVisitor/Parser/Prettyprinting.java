package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public class Prettyprinting extends Visitor {

	private int blockIndent = 0;
	@Override
	public void visit(BinaryComputing n) {
		n.getChild1().accept(this);
		System.out.print(" " + n.getOperation().textual + " ");
		n.getChild2().accept(this);
	}

	@Override
	public void visit(BoolskLiteral n) {
		System.out.print(n.getValue());
	}

	@Override
	public void visit(DecimaltalLiteral n) {
		System.out.print(n.getValue());
	}

	@Override
	public void visit(TekstLiteral n) {
		System.out.print("\"" + n.getValue() + "\"");
	}

	@Override
	public void visit(HeltalLiteral n) {
		System.out.print(n.getValue());
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
		System.out.print("\nrutine " +  n.getId() + ":");
		blockIndent++;
		for (AST stmt : n.getBody()) {
			System.out.print("\n");
			indent(blockIndent);
			stmt.accept(this);
		}
		blockIndent--;
		System.out.print("\n");
		indent(blockIndent);
		System.out.print(".");
	}

	@Override
	public void visit(FuncNode n) {
		System.out.println("\nkør " + n.getId());
	}

	@Override
	public void visit(IdNode n) {
		System.out.print(n.getName());
	}

	@Override
	public void visit(IfNode n) {
		System.out.print("hvis ");
		n.getExpr().accept(this);
		System.out.print(":");
		blockIndent++;
		for (AST child : n.getBody()) {
			System.out.print("\n");
			indent(blockIndent);
			child.accept(this);
		}
		blockIndent--;
		System.out.print("\n");
		indent(blockIndent);
		System.out.print(".");
	}

	@Override
	public void visit(LoopNode n) {
		System.out.print("gentag " + n.getId() + " ");
		n.getRepeats().accept(this);
		System.out.print(" gange");
	}

	@Override
	public void visit(PrintNode n) {
		System.out.print("print ");
		n.getValue().accept(this);
	}


	@Override
	public void visit(AssignNode n) {
		System.out.print("sæt " + n.getId() + " til ");
		n.getValue().accept(this);
	}

	@Override
	public void visit(UnaryComputing n) {
		System.out.print(n.getOperation().textual + " ");
		n.getChild().accept(this);
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
	public void visit(ConvertToFloat n) {

	}

	public void indent(int indents) {
		for (int i = 0; i < indents; i++) {
			System.out.print("    ");
		}
	}
}
