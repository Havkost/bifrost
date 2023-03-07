package ASTVisitor_AC_copy;

public class IntConsting extends AST {
	String val;
	
	IntConsting(String v){
		val = v;
	}
	
	public void accept(Visitor v){v.visit(this);}
	


}
