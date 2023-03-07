package ASTVisitor;

public class CodeGenerator extends Visitor {
	
	String code = "";
	
	public void emit(String c){
		code = code + c;
	}

	@Override
	void visit(Prog n) {
		// TODO Auto-generated method stub
		for(AST ast : n.prog){
			ast.accept(this);
		};
		System.out.println(code);

	}

	@Override
	void visit(SymDeclaring n) {
		// TODO Auto-generated method stub

	}

	@Override
	void visit(SymReferencing n) {
		// TODO Auto-generated method stub
		emit("l");
		emit(n.id + " ");

	}


}
