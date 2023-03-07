package ASTVisitor;

public class SymbolTableFilling extends Visitor {

	@Override
	void visit(Prog n) {
		// TODO Auto-generated method stub
		for(AST ast : n.prog){
			ast.accept(this);
		};

	}

	@Override
	void visit(SymDeclaring n) {
		// TODO Auto-generated method stub

	}

	@Override
	void visit(SymReferencing n) {
		// TODO Auto-generated method stub

	}
	
	private void error(String message) {
		throw new Error(message);
	}

}
