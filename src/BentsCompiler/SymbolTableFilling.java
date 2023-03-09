package BentsCompiler;

public class SymbolTableFilling extends Visitor {

	@Override
	void visit(Assigning n) {
		// TODO Auto-generated method stub
		n.child1.accept(this);

	}

	@Override
	void visit(Computing n) {
		// TODO Auto-generated method stub
		n.child1.accept(this); 
		n.child2.accept(this);

	}
	
	void visit(ConvertingToFloat n){
		n.child.accept(this);
	}

	@Override
	void visit(FloatConsting n) {
		// TODO Auto-generated method stub

	}

	@Override
	void visit(IntConsting n) {
		// TODO Auto-generated method stub

	}

	@Override
	void visit(Printing n) {
		// TODO Auto-generated method stub

	}

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
	void visit(FloatDcl n) {
		// TODO Auto-generated method stub
		if (AST.SymbolTable.get(n.id) == null) AST.SymbolTable.put(n.id,AST.FLTTYPE); 
		else error("variable " + n.id + " is already declared");

	}

	@Override
	void visit(IntDcl n) {
		// TODO Auto-generated method stub
		if (AST.SymbolTable.get(n.id) == null) AST.SymbolTable.put(n.id,AST.INTTYPE); 
		else error("variable " + n.id + " is already declared");

	}

	@Override
	void visit(SymReferencing n) {
		// TODO Auto-generated method stub

	}
	
	private void error(String message) {
		throw new Error(message);
	}

}
