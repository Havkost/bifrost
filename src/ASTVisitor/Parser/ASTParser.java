package ASTVisitor.Parser;

import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.Token;
import ASTVisitor.Lexer.TokenStream;
import ASTVisitor.Lexer.TokenType;

import static ASTVisitor.Lexer.TokenType.*;

import java.util.ArrayList;

public class ASTParser {
    private TokenStream ts;

    public ASTParser(CharStream s) {ts = new TokenStream(s);}



    public AST Program() {
        Program theAST = new Program(new ArrayList<AST>());
        if(ts.peek() != EOF) {
            Lines();
            expect(EOF);
        } else error("error, empty program");
        return theAST;
    }

    public void Lines() {
        if (ts.peek() == GEM || ts.peek() == RUTINE || ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR || ts.peek() == HVIS) {
            Line();
            expect(NEWLINE);
            Lines();
        } else error("FORVENTEDE gem, rutine, sæt, gentag, kør, ELLER hvis");
    }

    public ArrayList<AST> Line() {
        ArrayList<AST> dcllist = new ArrayList<>();
        ArrayList<AST> stmlist = new ArrayList<>();
        if (ts.peek() == GEM || ts.peek() == RUTINE) {


        } else if (ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR || ts.peek() == HVIS) {

        } else error("FORVENTEDE gem, rutine, sæt, gentag, kør, ELLER hvis");
    }

    public void Dcl() {
        if (ts.peek() == GEM) {

        } else if (ts.peek() == RUTINE) {

        } else error("FORVENTEDE gem ELLER rutine");
    }

    public void Fnc_dcl() {

    }

    public void Stmt() {
        if (ts.peek() == SET) {

        } else if (ts.peek() == GENTAG) {

        } else if (ts.peek() == KOR) {
        } else if (ts.peek() == HVIS) {
        } else error("FORVENTEDE sæt, gentag, kør ELLER hvis");
    }

    public void Stmts() {
        if (ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR || ts.peek() == HVIS) {
        } else if (ts.peek() == NEWLINE) {
            // do nothing (empty set)
        } else error("FORVENTEDE sæt, gentag, kør ELLER hvis");
    }

    public void Assign() {

    }

    public void Loop() {

    }

    public void Func() {

    }

    public void If() {

    }

    /**
     * EXPRESSION
     */
    public void Expr() {
        if (ts.peek() == ID || ts.peek() == HELTAL_LIT || ts.peek() == IKKE || ts.peek() == LPAREN
                || ts.peek() == TEKST_LIT || ts.peek() == DECIMALTAL_LIT || ts.peek() == BOOLSK_LIT) {
        } else error("FORVENTEDE id, heltal, tekst, decimaltal, boolsk, ikke, ELLER (");
    }

    public void Or_expr() {

    }

    public void Or_expr2() {
        if (ts.peek() == ELLER) {

        } else if (ts.peek() == RPAREN || ts.peek() == BLOCKSTART) {
            // produce nothing
        } else error("FORVENTEDE eller");
    }

    public void And_expr() {

    }

    public void And_expr2() {
        if (ts.peek() == OG) {

        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == RPAREN) {
            // Do nothing (empty set)
        } else error("FORVENTEDE og");
    }

    public void Equality_expr() {

    }

    public void Equality_expr2() {
        if (ts.peek() == ER) {

        } else if (ts.peek() == IKKEER) {

        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == RPAREN) {
            // Do nothing, empty set
        } else error("FORVENTEDE er ELLER ikke er");
    }

    public void Rel_expr() {

    }

    public void Rel_expr2() {
        if (ts.peek() == LESSER) {

        } else if (ts.peek() == GREATER) {

        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG
                || ts.peek() == RPAREN || ts.peek() == ER || ts.peek() == IKKE) {
            // Do nothing (empty set)
        } else error("FORVENTEDE < ELLER >");
    }

    public void Sum_expr() {

    }

    public void Sum_expr2() {
        if (ts.peek() == PLUS) {

        } else if (ts.peek() == MINUS) {

        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == ER || ts.peek() == IKKE
                || ts.peek() == LESSER || ts.peek() == GREATER || ts.peek() == RPAREN) {
            // Do nothing, empty set
        } else error("FORVENTEDE + ELLER -");
    }

    public void Product_expr() {

    }

    public void Product_expr2() {
        if (ts.peek() == TIMES) {

        } else if (ts.peek() == DIVIDE) {

        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == ER
                || ts.peek() == LESSER || ts.peek() == GREATER || ts.peek() == RPAREN
                || ts.peek() == PLUS || ts.peek() == MINUS) {
            // Do nothing (empty set)
        } else error("FORVENTEDE * ELLER /");
    }

    public void Not_expr() {
        if (ts.peek() == IKKE) {

        } else Factor();

    }

    //TODO skriv ordentlig fejlbesked
    public void Factor() {
        if (ts.peek() == LPAREN) {

        } else if (ts.peek() == HELTAL_LIT || ts.peek() == TEKST_LIT || ts.peek() == DECIMALTAL_LIT || ts.peek() == BOOLSK_LIT) {
        } else if (ts.peek() == ID) {
        } else error("FORVENTEDE (, heltal, decimaltal, tekst ELLER boolsk");
    }

    public void Value() {
        if (ts.peek() == TEKST_LIT) {
        } else if (ts.peek() == HELTAL_LIT) {
        } else if (ts.peek() == DECIMALTAL_LIT) {
        } else if (ts.peek() == BOOLSK_LIT) {
        } else error("FORVENTEDE tekst, heltal, decimaltal ELLER boolsk");
    }

    public void Type() {
        if (ts.peek() == TEKST) {
        } else if (ts.peek() == HELTAL) {
        } else if (ts.peek() == DECIMALTAL) {
        } else if (ts.peek() == BOOLSK) {
        } else error("TYPE IKKE GODKENDT. FORVENTEDE tekst, heltal, decimaltal, ELLER boolsk");
    }

    private void expect(TokenType type) {
        Token t = ts.advance();
        if (t.getType() != type) {
            throw new Error("Expected type "
                    + type
                    + " but received type "
                    + t.getType());

        }
    }

    private void error(String message) {
        throw new Error(message);
    }
}
}
