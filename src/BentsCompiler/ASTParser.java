	package BentsCompiler;

import static BentsCompiler.Token.*;

import java.util.ArrayList;

public class ASTParser {
	private TokenStream ts;

	public ASTParser(CharStream s) {
		ts = new TokenStream(s);
	}


	public AST Prog() {
		Prog itsAST = new Prog(new ArrayList<AST>());
		if (ts.peek() == FLTDCL || ts.peek() == INTDCL || ts.peek() == ID || ts.peek() == PRINT || ts.peek() == EOF) {
			ArrayList<AST> dcllist = Dcls();
			ArrayList<AST> stmlist = Stmts();
			expect(EOF); 
			if (dcllist != null) itsAST.prog.addAll(dcllist);
			if (stmlist != null) itsAST.prog.addAll(stmlist);
		}
		else error("expected floatdcl, intdcl, id, print, or eof");
		return itsAST;
	}

	public ArrayList<AST> Dcls() {
		ArrayList<AST> astlist = new ArrayList<AST>();
		if (ts.peek() == FLTDCL || ts.peek() == INTDCL) {
			AST dcl = Dcl();
			ArrayList<AST> dcls = Dcls();
			astlist.add(dcl);
			astlist.addAll(dcls);
		}
		else if (ts.peek() == ID || ts.peek() == PRINT || ts.peek() == EOF) {
			// Do nothing for lambda-production
		}
		else error("expected floatdcl, intdcl, id, print, or eof");
		return astlist;
	}

	public AST Dcl() {
		AST itsAst = null;
		if (ts.peek() == FLTDCL) {
			expect(FLTDCL);
			Token t = expect(ID);
			itsAst = new FloatDcl(t.val);
		}
		else if (ts.peek() == INTDCL) {
			expect(INTDCL);
			Token t = expect(ID);
			itsAst = new IntDcl(t.val);
		}
		else error("expected float or int declaration");
		return itsAst;
	}

	/**
	 * Figure 2.7 code
	 */
	public ArrayList<AST> Stmts() {
		ArrayList<AST> astlist = new ArrayList<AST>();
		if (ts.peek() == ID || ts.peek() == PRINT) {
			AST stmt = Stmt();
			ArrayList<AST> stms = Stmts();
			astlist.add(stmt);
			astlist.addAll(stms);
		}
		else if (ts.peek() == EOF) {
			// Do nothing for lambda-production
		}
		else error("expected id, print, or eof");
		return astlist;

	}

	public AST Stmt() {
		AST itsAst = null;
		if (ts.peek() == ID) {
			Token tid = expect(ID);
			expect(ASSIGN);
			AST val = Val();
			Computing expr = Expr();
			if (expr == null) itsAst = new Assigning(tid.val,val); 
			else {expr.child1 = val; itsAst = new Assigning(tid.val, expr);};
		}
		else if (ts.peek() == PRINT) {
			expect(PRINT);
			Token tid = expect(ID);
			itsAst = new Printing(tid.val);
		}
		else error("expected id or print");
		return itsAst;

	}

	public Computing Expr() {
		Computing itsAst = null;
		if (ts.peek() == PLUS) {
			expect(PLUS);
			AST val = Val();
			Computing expr = Expr();
			//The construction of the AST is a little messy as the grammar for the ac language is Expr -> (+|-) Val Expr
			//which will be used in the Stm -> Id assign Val Expr production. However, we really want the AST
			//to have an Assigning node corresponding to Id assign Expr where Expr -> Val (+|-) Expr i.e. a Computing node
			//thus we create a Computing node in this parse method with an empty left child and 
			//in the parse method for STM we adjust the AST with the correct left child
			if (expr != null) {expr.child1 = val; itsAst = new Computing("+",null, expr);} 
			else itsAst = new Computing("+",null,val);
		}
		else if (ts.peek() == MINUS) {
			expect(MINUS);
			AST val = Val();
			Computing expr = Expr();
			if (expr != null) {expr.child1 = val; itsAst = new Computing("-",null, expr);} 
			else itsAst = new Computing("-",null,val);

		}
		else if (ts.peek() == ID || ts.peek() == PRINT || ts.peek() == EOF) {
			// Do nothing for lambda-production
		}
		else error("expected plus, minus, id, print, or eof");
		return itsAst;

	}

	public AST Val() {
		AST itsAst = null;
		if (ts.peek() == ID) {
			Token tid = expect(ID);
			itsAst = new SymReferencing(tid.val);
		}
		else if (ts.peek() == INUM) {
			Token tid = expect(INUM);
			itsAst = new IntConsting(tid.val);
		}
		else if (ts.peek() == FNUM) {
			Token tid = expect(FNUM);
			itsAst = new FloatConsting(tid.val);
		}
		else error("expected id, inum, or fnum");
		return itsAst;

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
