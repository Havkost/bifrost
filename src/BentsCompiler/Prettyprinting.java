package BentsCompiler;

public class Prettyprinting extends Visitor {

	@Override
	void visit(Assigning n) {
		// TODO Auto-generated method stub
		System.out.print(n.id + " = " );
		n.child1.accept(this);
		System.out.print(" ");

	}

	@Override
	void visit(Computing n) {
		// TODO Auto-generated method stub
		n.child1.accept(this); 
		System.out.print(" " + n.operation + " ");
		n.child2.accept(this);

	}

	void visit(ConvertingToFloat n){
		System.out.print(" i2f ");
		n.child.accept(this);
	}
	
	@Override
	void visit(FloatConsting n) {
		// TODO Auto-generated method stub
		System.out.print(n.val);

	}

	@Override
	void visit(IntConsting n) {
		// TODO Auto-generated method stub
		System.out.print(n.val);

	}

	@Override
	void visit(Printing n) {
		// TODO Auto-generated method stub
		System.out.print("p " + n.id + " ");

	}

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
	void visit(FloatDcl n) {
		// TODO Auto-generated method stub
		System.out.print("f " + n.id + " ");

	}

	@Override
	void visit(IntDcl n) {
		// TODO Auto-generated method stub
			System.out.print("i " + n.id + " ");

	}

	@Override
	void visit(SymReferencing n) {
		// TODO Auto-generated method stub
		System.out.print(n.id);

	}

}
