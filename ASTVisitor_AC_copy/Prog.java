package ASTVisitor_AC_copy;

import java.util.ArrayList;

;

public class Prog extends AST {
	
	ArrayList<AST> prog;
	
	Prog(ArrayList<AST> prg){
		prog = prg;
	}
	
	public void accept(Visitor v){v.visit(this);}
	
	

}
