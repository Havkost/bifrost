package ASTVisitor_AC_copy;

// $Id: Token.java 31 2010-01-07 20:25:05Z cytron $

public class Token {
	
	//
	// Java enums would be better for this,
	//   but these statics allow a closer match
	//   of the code here to the code in the text book
	//
	public final static int
		ID       = 0,
		FLTDCL   = 1,
		INTDCL   = 2,
		PRINT    = 3,
		ASSIGN   = 4,
		PLUS     = 5,
		MINUS    = 6,
		EOF      = 7,
		INUM     = 8,
		FNUM     = 9;
	
	public final static String[] token2str = new String[] {
			"id", "fltdcl", "intdcl", "print", "assign", "plus", "minus", "$", "inum", "fnum"
	};
	
	public final int    type;
	public final String val;
	
	public Token(int type) {
		this(type, "");
	}
	
	public Token(int type, String val) {
		this.type = type;
		this.val  = val;
	}
		
	public String toString() {
		return "Token type\t" + token2str[type] + "\tval\t" + val;
	}
}
