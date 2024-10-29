package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public interface Visitor {
	void visit(AssignNode n);
	void visit(BinaryComputing n);
	void visit(BoolskLiteral n);
	void visit(DecimaltalLiteral n);
	void visit(FuncDclNode n);
	void visit(FuncNode n);
	void visit(HeltalLiteral n);
	void visit(IdNode n);
	void visit(IfNode n);
	void visit(LoopNode n);
	void visit(PrintNode n);
	void visit(ProgramNode n);
	void visit(TekstLiteral n);
	void visit(UnaryComputing n);
	void visit(TekstDcl n);
	void visit(HeltalDcl n);
	void visit(DecimaltalDcl n);
	void visit(BoolskDcl n);
	void visit(DeviceNode n);
	void visit(KlokkenNode n);
    void visit(TidNode n);
	void visit(TidDcl n);

	void visit(CommentNode commentNode);
}
