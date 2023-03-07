package ASTVisitor;

public class Prettyprinting extends Visitor {

	@Override
	void visit(Prog n) {
		// TODO Auto-generated method stub
		for(AST ast : n.prog){
			ast.accept(this);
		};
		System.out.println();

	}

	@Override
	void visit(SymDeclaring n) {
		// TODO Auto-generated method stub

	}

	@Override
	void visit(SymReferencing n) {
		// TODO Auto-generated method stub
		System.out.print(n.id);

	}

}
