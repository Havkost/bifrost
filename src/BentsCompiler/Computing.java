package acASTVisitor;

public class Computing extends AST {
	String operation;
	AST child1;
	AST child2;
	
	Computing(String op, AST ch1, AST ch2){
		child1 = ch1;
		child2 = ch2;
		operation = op;
		
	}
	
	public void accept(Visitor v){v.visit(this);}
	
	
}
