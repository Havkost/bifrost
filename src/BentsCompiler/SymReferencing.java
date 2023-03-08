package acASTVisitor;

public class SymReferencing extends AST {
	String id;
	
	SymReferencing(String i){
		id = i;
	}
	
	public void accept(Visitor v){v.visit(this);}
	


}
