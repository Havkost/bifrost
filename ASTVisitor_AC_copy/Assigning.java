package ASTVisitor_AC_copy;

public class Assigning extends AST {
	String id;
	AST child1;
	
	//Note I cheated, in the book an Assign node has two children, where the first child always is a SymReferencing node
	
	Assigning(String i, AST ch1){
		id = i;
		child1 = ch1;
		
	}
	
	public void accept(Visitor v){v.visit(this);}
	

}
