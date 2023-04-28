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
		emit(n.getId());
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
		if (n.getId() instanceof IdNode)
			emit("sæt " + ((IdNode) n.getId()).getId() + " til ");
		else
			emit("sæt " + ((FieldNode) n.getId()).getId() + " for " + ((FieldNode) n.getId()).getParentId() + " til ");
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
		if (n.getParentId() != null)
			emit("tekst ");
		else
			emit("gem tekst ");
		n.getValue().accept(this);
		emit(" som " + n.getId());
	}

	@Override
	public void visit(HeltalDcl n) {
		if (n.getParentId() != null)
			emit("heltal ");
		else
			emit("gem heltal ");
		n.getValue().accept(this);
		emit(" som " + n.getId());
	}

	@Override
	public void visit(DecimaltalDcl n) {
		if (n.getParentId() != null)
			emit("decimaltal ");
		else
			emit("gem decimaltal ");
		n.getValue().accept(this);
		emit(" som " + n.getId());
	}

	@Override
	public void visit(BoolskDcl n) {
		if (n.getParentId() != null)
			emit("boolsk ");
		else
			emit("gem boolsk ");
		n.getValue().accept(this);
		emit(" som " + n.getId());
	}

	@Override
	public void visit(FieldDclNode n) {
		emit(n.getType() + " ");
		n.getValue().accept(this);
		emit(" som " + n.getId());	}

	public void indent(int indents) {
		for (int i = 0; i < indents; i++) {
			emit("    ");
		}
	}
	@Override
	public void visit(FieldNode n) {
		emit(n.getId() + " for " + n.getParentId());
	}

	@Override
	public void visit(DeviceNode n) {
		emit("gem enhed \"" + n.getEndpoint() + "\" med:\n");
		blockIndent++;
		n.getFields().forEach((field) -> {
			indent(blockIndent);
			field.accept(this);
			emit("\n");
		});
		blockIndent--;
		indent(blockIndent);
		emit("som " + n.getId() + "\n");
	}

	public String getCode() {
		return code;
	}
}

