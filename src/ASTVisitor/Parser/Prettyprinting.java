package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;

public class Prettyprinting extends Visitor {

	@Override
	void visit(Assigning n) {
		// TODO Auto-generated method stub
		System.out.print(n.id + " = " );
		n.child1.accept(this);
		System.out.print(" ");

	}

	@Override
	void visit(BinaryComputing n) {
		// TODO Auto-generated method stub
		n.getChild1().accept(this);
		System.out.print(" " + n.getOperation() + " ");
		n.getChild2().accept(this);

	}

	@Override
	void visit(ProgramNode n) {
		// TODO Auto-generated method stub

		for(AST ast : n.getChild()){
			ast.accept(this);
		};
		System.out.println();

	}

	@Override
	void visit(SymDeclaring n) {
		// TODO Auto-generated method stub

	}

}