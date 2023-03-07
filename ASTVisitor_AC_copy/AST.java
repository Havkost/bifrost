package ASTVisitor_AC_copy;

import java.util.Hashtable;

public abstract class AST {
	
	public final static int
	FLTTYPE   = 0,
	INTTYPE   = 1;
	
	public static Hashtable<String,Integer> SymbolTable = new Hashtable<>();
	
	AST(){
		//for(int ch = 'a'; ch <= 'z'; ch++){AST.SymbolTable.put("" + ch,null);};
	}
	
	public Integer type = null;
	
	
	public abstract void accept(Visitor v);

}
