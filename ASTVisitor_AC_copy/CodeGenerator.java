package ASTVisitor_AC_copy;

public class CodeGenerator extends Visitor {
	
	String code = "";
	
	public void emit(String c){
		code = code + c;
	}

	@Override
	void visit(Assigning n) {
		// TODO Auto-generated method stub
		n.child1.accept(this);
		emit(" s");
		emit(n.id);
		emit(" 0 k ");

	}

	@Override
	void visit(Computing n) {
		// TODO Auto-generated method stub
		n.child1.accept(this); 
		n.child2.accept(this);
		emit(n.operation);

	}
	
	void visit(ConvertingToFloat n){
		n.child.accept(this);
		emit(" 5 k ");
		
	}
	
	@Override
	void visit(FloatConsting n) {
		// TODO Auto-generated method stub
		emit(" " + n.val + " ");

	}

	@Override
	void visit(IntConsting n) {
		// TODO Auto-generated method stub
		emit(" " + n.val + " ");

	}

	@Override
	void visit(Printing n) {
		// TODO Auto-generated method stub
		emit("l");
		emit(n.id);
		emit(" p ");
		emit("si ");

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
	void visit(FloatDcl n) {
		// TODO Auto-generated method stub
		

	}

	@Override
	void visit(IntDcl n) {
		// TODO Auto-generated method stub
			

	}

	@Override
	void visit(SymReferencing n) {
		// TODO Auto-generated method stub
		emit("l");
		emit(n.id + " ");

	}


}
