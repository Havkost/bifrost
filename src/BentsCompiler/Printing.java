package acASTVisitor;

public class Printing extends AST {
	String id;
	
	Printing(String i){
		id = i;
	}
	
	public void accept(Visitor v){v.visit(this);}
	


}
