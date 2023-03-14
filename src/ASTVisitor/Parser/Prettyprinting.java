package ASTVisitor.Parser;

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

	@Override
	void visit(Prog n) {
		// TODO Auto-generated method stub
		for(Node node : n.prog){
			node.accept(this);
		};
		System.out.println();

	}

	@Override
	void visit(SymDeclaring n) {
		// TODO Auto-generated method stub

	}

}
