package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public class Prettyprinting extends Visitor {

	private int blockIndent = 0;
	private String code = "";
	private final boolean print;

	public Prettyprinting(boolean print) {
		this.print = print;
	}

	@Override
	public void visit(BinaryComputing n) {
		n.getChild1().accept(this);
		emit(" " + n.getOperation().textual + " ");
		n.getChild2().accept(this);
	}

	public void emit(String s) {
		code += s;
	}

	@Override
	public void visit(BoolskLiteral n) {
		emit(n.getValue());
	}

	@Override
	public void visit(DecimaltalLiteral n) {
		emit(n.getValue());
	}

	@Override
	public void visit(TekstLiteral n) {
		emit("\"" + n.getValue() + "\"");
	}

	@Override
	public void visit(HeltalLiteral n) {
		emit(n.getValue());
	}

	@Override
	public void visit(ProgramNode n) {
		for(AST ast : n.getChild()){
			ast.accept(this);
			emit("\n");
		}

		if (print) System.out.println(code);
	}
	@Override
	public void visit(FuncDclNode n) {
		emit("\nrutine " +  n.getId() + ":");
		blockIndent++;
		for (AST stmt : n.getBody()) {
			emit("\n");
			indent(blockIndent);
			stmt.accept(this);
		}
		blockIndent--;
		emit("\n");
		indent(blockIndent);
		emit(".");
	}

	@Override
	public void visit(FuncNode n) {
		emit("kør " + n.getId());
	}

	@Override
	public void visit(IdNode n) {
		emit(n.getName());
	}

	@Override
	public void visit(IfNode n) {
		emit("hvis ");
		n.getExpr().accept(this);
		emit(":");
		blockIndent++;
		for (AST child : n.getBody()) {
			emit("\n");
			indent(blockIndent);
			child.accept(this);
		}
		blockIndent--;
		emit("\n");
		indent(blockIndent);
		emit(".");
	}

	@Override
	public void visit(LoopNode n) {
		emit("gentag " + n.getId() + " ");
		n.getRepeats().accept(this);
		emit(" gange");
	}

	@Override
	public void visit(PrintNode n) {
		emit("print ");
		n.getValue().accept(this);
	}

	@Override
	public void visit(AssignNode n) {
		emit("sæt " + n.getId() + " til ");
		n.getValue().accept(this);
	}

	@Override
	public void visit(UnaryComputing n) {
		if(n.getOperation().equals(AST.Operators.PAREN)) {
			emit("(");
			n.getChild().accept(this);
			emit(")");
		} else {
			emit(n.getOperation().textual + " ");
			n.getChild().accept(this);
		}
	}

	@Override
	public void visit(TekstDcl n) {
		emit("gem tekst ");
		n.getValue().accept(this);
		emit(" som " + n.getId());
	}

	@Override
	public void visit(HeltalDcl n) {
		emit("gem heltal ");
		n.getValue().accept(this);
		emit(" som " + n.getId());
	}

	@Override
	public void visit(DecimaltalDcl n) {
		emit("gem decimaltal ");
		n.getValue().accept(this);
		emit(" som " + n.getId());
	}

	@Override
	public void visit(BoolskDcl n) {
		emit("gem boolsk ");
		n.getValue().accept(this);
		emit(" som " + n.getId());
	}

	public void indent(int indents) {
		for (int i = 0; i < indents; i++) {
			emit("    ");
		}
	}

	public String getCode() {
		return code;
	}
}
