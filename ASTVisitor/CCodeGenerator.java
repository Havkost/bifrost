package ASTVisitor;

public class CCodeGenerator extends Visitor {

String code = "";
	
	public void emit(String c){
		code = code + c;
	}

	@Override
	void visit(Prog n) {
		// TODO Auto-generated method stub
		
		emit("#include < stdio.h>\n\n");
	    emit("void main()\n{\n");
		for(AST ast : n.prog){
			ast.accept(this);
		};
		emit("return 0;");
		emit("\n}");
		
		System.out.println(code);

	}

	@Override
	void visit(SymDeclaring n) {
		// TODO Auto-generated method stub

	}

	@Override
	void visit(SymReferencing n) {
		// TODO Auto-generated method stub
		emit(" " + n.id + " ");

	}

}
