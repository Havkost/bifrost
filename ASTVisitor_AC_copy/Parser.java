package ASTVisitor_AC_copy;

import static ASTVisitor.Token.*;

// $Id: Parser.java 25 2010-01-07 17:03:40Z cytron $

/**
 * Recursive-descent parser based on the grammar given
 *   in Figure 2.1
 * @author cytron
 *
 */
public class Parser {

	private TokenStream ts;

	public Parser(CharStream s) {
		ts = new TokenStream(s);
	}


	public void Prog() {
		if (ts.peek() == FLTDCL || ts.peek() == INTDCL || ts.peek() == ID || ts.peek() == PRINT || ts.peek() == EOF) {
			Dcls();
			Stmts();
			expect(EOF);
		}
		else error("expected floatdcl, intdcl, id, print, or eof");
	}

	public void Dcls() {
		if (ts.peek() == FLTDCL || ts.peek() == INTDCL) {
			Dcl();
			Dcls();
		}
		else if (ts.peek() == ID || ts.peek() == PRINT || ts.peek() == EOF) {
			// Do nothing for lambda-production
		}
		else error("expected floatdcl, intdcl, id, print, or eof");
	}

	public void Dcl() {
		if (ts.peek() == FLTDCL) {
			expect(FLTDCL);
			expect(ID);
		}
		else if (ts.peek() == INTDCL) {
			expect(INTDCL);
			expect(ID);
		}
		else error("expected float or int declaration");
	}

	/**
	 * Figure 2.7 code
	 */
	public void Stmts() {
		if (ts.peek() == ID || ts.peek() == PRINT) {
			Stmt();
			Stmts();
		}
		else if (ts.peek() == EOF) {
			// Do nothing for lambda-production
		}
		else error("expected id, print, or eof");

	}

	public void Stmt() {
		if (ts.peek() == ID) {
			expect(ID);
			expect(ASSIGN);
			Val();
			Expr();
		}
		else if (ts.peek() == PRINT) {
			expect(PRINT);
			expect(ID);
		}
		else error("expected id or print");

	}

	public void Expr() {
		if (ts.peek() == PLUS) {
			expect(PLUS);
			Val();
			Expr();
		}
		else if (ts.peek() == MINUS) {
			expect(MINUS);
			Val();
			Expr();

		}
		else if (ts.peek() == ID || ts.peek() == PRINT || ts.peek() == EOF) {
			// Do nothing for lambda-production
		}
		else error("expected plus, minus, id, print, or eof");

	}

	public void Val() {
		if (ts.peek() == ID) {
			expect(ID);
		}
		else if (ts.peek() == INUM) {
			expect(INUM);
		}
		else if (ts.peek() == FNUM) {
			expect(FNUM);
		}
		else error("expected id, inum, or fnum");

	}

	private void expect(int type) {
		Token t = ts.advance();
		if (t.type != type) {
			throw new Error("Expected type " 
					+ Token.token2str[type]
					                  + " but received type "
					                  + Token.token2str[t.type]);

		}
	}

	private void error(String message) {
		throw new Error(message);
	}

}
