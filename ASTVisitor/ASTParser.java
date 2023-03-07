package ASTVisitor;

import static ASTVisitor.Token.ASSIGN;
import static ASTVisitor.Token.EOF;
import static ASTVisitor.Token.FLTDCL;
import static ASTVisitor.Token.FNUM;
import static ASTVisitor.Token.ID;
import static ASTVisitor.Token.INTDCL;
import static ASTVisitor.Token.INUM;
import static ASTVisitor.Token.MINUS;
import static ASTVisitor.Token.PLUS;
import static ASTVisitor.Token.PRINT;

import java.util.ArrayList;

public class ASTParser {
	private TokenStream ts;

	public ASTParser(CharStream s) {
		ts = new TokenStream(s);
	}


	public AST Prog() {
		return null;
	}


	private Token expect(int type) {
		Token t = ts.advance();
		if (t.type != type) {
			throw new Error("Expected type " 
					+ Token.token2str[type]
					                  + " but received type "
					                  + Token.token2str[t.type]);

		};
		return t;
	}

	private void error(String message) {
		throw new Error(message);
	}

}
