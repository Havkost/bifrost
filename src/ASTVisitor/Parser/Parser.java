package ASTVisitor.Parser;

import ASTVisitor.Lexer.Token;
import ASTVisitor.Lexer.TokenStream;
import static ASTVisitor.Lexer.TokenType.*;

import java.util.ArrayList;

import ASTVisitor.Lexer.*;

public class Parser {

    private TokenStream ts;

    public Parser(CharStream s) {
        ts = new TokenStream(s);
    }

    public void Program() {
        if (ts.peek() == EOF) return;
        Lines();
    }

    public void Lines() {
        if (ts.peek() == GEM || ts.peek() == RUTINE || ts.peek() == SAET || ts.peek() == GENTAG || ts.peek() == KOER || ts.peek() == HVIS) {
            Line();
            expect(NEWLINE);
            Lines();
        } else if (ts.peek() == EOF) {
        } else if (ts.peek() == EOF) {
            // do nothing (empty set)
        } else error("FORVENTEDE gem, rutine, sæt, gentag, kør, ELLER hvis");
    }

    public void Line() {
        if (ts.peek() == GEM || ts.peek() == RUTINE) {
            Dcl();
        } else if (ts.peek() == SAET || ts.peek() == GENTAG || ts.peek() == KOER || ts.peek() == HVIS) {
            Stmt();
        } else error("FORVENTEDE gem, rutine, sæt, gentag, kør, ELLER hvis");
    }

    public void Dcl() {
        if (ts.peek() == GEM) {
            expect(GEM);
            Type();
            Value();
            expect(SOM);
            expect(ID);
        }
        if (ts.peek() == RUTINE) {
            Fnc_dcl();
        } else error("FORVENTEDE gem ELLER rutine");
    }

    public void Fnc_dcl() {
        expect(RUTINE);
        expect(ID);
        expect(BLOCKSTART);
        expect(NEWLINE);
        Stmts();
        expect(BLOCKSLUT);
    }

    public void Stmt() {
        if (ts.peek() == SAET) {
            Assign();
        } else if (ts.peek() == GENTAG) {
            Loop();
        } else if (ts.peek() == KOER) {
            Func();
        } else if (ts.peek() == HVIS) {
            If();
        } else error("FORVENTEDE sæt, gentag, kør ELLER hvis");
        expect(NEWLINE); //TODO Er denne nødvendig? - JA tror Jack
    }

    public void Stmts() {
        if (ts.peek() == SAET || ts.peek() == GENTAG || ts.peek() == KOER || ts.peek() == HVIS) {
            Stmt();
            Stmts();
        } else if (ts.peek() == NEWLINE) {
            // do nothing (empty set)
        } else error("FORVENTEDE sæt, gentag, kør ELLER hvis");
    }

    public void Assign() {
        expect(SAET);
        expect(ID);
        expect(TIL);
        Value();
    }

    public void Loop() {
        expect(GENTAG);
        Func();
        expect(HELTAL_LIT);
        expect(GANGE);
    }

    public void Func() {
        expect(KOER);
        expect(ID);
    }

    public void If() {
        expect(HVIS);
        Expr();
        expect(BLOCKSTART);
        expect(NEWLINE);
        Stmts();
        expect(BLOCKSLUT);
    }

    /**
     * EXPRESSION
     */
    public void Expr() {
        if (ts.peek() == ID || ts.peek() == HELTAL_LIT || ts.peek() == IKKE || ts.peek() == LPAREN
                || ts.peek() == TEKST_LIT || ts.peek() == DECIMALTAL_LIT || ts.peek() == BOOLSK_LIT) {
            Or_expr();
        } else error("FORVENTEDE id, heltal, tekst, decimaltal, boolsk, ikke, ELLER (");
    }

    public void Or_expr() {
        And_expr();
        Or_expr2();
    }

    public void Or_expr2() {
        if (ts.peek() == ELLER) {
            expect(ELLER);
            And_expr();
            Or_expr2();
        } else if (ts.peek() == RPAREN || ts.peek() == BLOCKSTART) {
            // produce nothing
        } else error("FORVENTEDE eller");
    }

    public void And_expr() {
        Equality_expr();
        And_expr2();
    }

    public void And_expr2() {
        if (ts.peek() == OG) {
            expect(OG);
            Equality_expr();
            And_expr2();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == RPAREN) {
            // Do nothing (empty set)
        } else error("FORVENTEDE og");
    }

    public void Equality_expr() {
        Rel_expr();
        Equality_expr2();
    }

    public void Equality_expr2() {
        if (ts.peek() == ER) {
            expect(ER);
            Rel_expr();
        } else if (ts.peek() == IKKE) {
            expect(IKKE);
            expect(ER);
            Rel_expr();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == RPAREN) {
            // Do nothing, empty set
        } else error("FORVENTEDE er ELLER ikke er");
    }

    public void Rel_expr() {
        Sum_expr();
        Rel_expr2();
    }

    public void Rel_expr2() {
        if (ts.peek() == LESSER) {
            expect(LESSER);
            Sum_expr();
        } else if (ts.peek() == GREATER) {
            expect(GREATER);
            Sum_expr();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG
                || ts.peek() == RPAREN || ts.peek() == ER || ts.peek() == IKKE) {
            // Do nothing (empty set)
        } else error("FORVENTEDE < ELLER >");
    }

    public void Sum_expr() {
        Product_expr();
        Sum_expr2();
    }

    public void Sum_expr2() {
        if (ts.peek() == PLUS) {
            expect(PLUS);
            Product_expr();
            Sum_expr2();
        } else if (ts.peek() == MINUS) {
            expect(MINUS);
            Product_expr();
            Sum_expr2();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == ER || ts.peek() == IKKE
                || ts.peek() == LESSER || ts.peek() == GREATER || ts.peek() == RPAREN) {
            // Do nothing, empty set
        } else error("FORVENTEDE + ELLER -");
    }

    public void Product_expr() {
        Not_expr();
        Product_expr2();
    }

    public void Product_expr2() {
        if (ts.peek() == TIMES) {
            expect(TIMES);
            Not_expr();
            Product_expr2();
        } else if (ts.peek() == DIVIDE) {
            expect(DIVIDE);
            Not_expr();
            Product_expr2();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == ER
                || ts.peek() == LESSER || ts.peek() == GREATER || ts.peek() == RPAREN
                || ts.peek() == PLUS || ts.peek() == MINUS) {
            // Do nothing (empty set)
        } else error("FORVENTEDE * ELLER /");
    }

    public void Not_expr() {
        if (ts.peek() == IKKE) {
            expect(IKKE);
            Factor();
        } else Factor();

    }
    
    public void Factor() {
        if (ts.peek() == LPAREN) {
            expect(LPAREN);
            Expr();
            expect(RPAREN);
        } else if (ts.peek() == HELTAL_LIT || ts.peek() == TEKST_LIT || ts.peek() == DECIMALTAL_LIT || ts.peek() == BOOLSK_LIT) {
            Value();
        } else if (ts.peek() == ID) {
            expect(ID);
        } else error("FORVENTEDE (, heltal, decimaltal, tekst ELLER boolsk");
    }

    public void Value() {
        if (ts.peek() == TEKST_LIT) {
            expect(TEKST_LIT);
        } else if (ts.peek() == HELTAL_LIT) {
            expect(HELTAL_LIT);
        } else if (ts.peek() == DECIMALTAL_LIT) {
            expect(DECIMALTAL_LIT);
        } else if (ts.peek() == BOOLSK_LIT) {
            expect(BOOLSK_LIT);
        } else error("FORVENTEDE tekst, heltal, decimaltal ELLER boolsk");
    }

    public void Type() {
        if (ts.peek() == TEKST_DCL) {
            expect(TEKST_DCL);
        } else if (ts.peek() == HELTAL_DCL) {
            expect(HELTAL_DCL);
        } else if (ts.peek() == DECIMALTAL_DCL) {
            expect(DECIMALTAL_DCL);
        } else if (ts.peek() == BOOLSK_DCL) {
            expect(BOOLSK_DCL);
        } else error("TYPE IKKE GODKENDT. FORVENTEDE tekst, heltal, decimaltal, ELLER boolsk");
    }

    private boolean peekAndExpectTokens(ArrayList<TokenType> tokens) {
        for (TokenType token : tokens) {
            if (ts.peek() == token) {
                expect(token);
                return true;
            }
        }
        return false;
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

