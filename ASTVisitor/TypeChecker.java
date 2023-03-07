package ASTVisitor;

public class TypeChecker extends Visitor {

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
		n.type = AST.SymbolTable.get(n.id);

	}
	
	private void error(String message) {
		throw new Error(message);
	}
	
	/* Inlined in 
	 * private int consistent(AST c1, AST c2){
		int m = generalize(c1.type,c2.type);
		convert(c1,m);
		convert(c2,m);
		
	}*/
	
	private int generalize(int t1, int t2){
		if (t1 == AST.FLTTYPE || t2 == AST.FLTTYPE) return AST.FLTTYPE; else return AST.INTTYPE;
	}

}
