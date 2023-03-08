
package ASTVisitor.Parser;

import java.util.ArrayList;
import java.util.List;

import static ASTVisitor.Parser.TokenType.*;

public class Parser {

    /* TODO: Lav Tokenstream, CharStream, ScannerCode, Token classes */

    private TokenStream ts;

    public Parser(CharStream s) {
        ts = new TokenStream(s);
    }

    public void Program() {
        if (ts.peek() == EOF) return;
        Line();
        Program();
    }

    public void Line() {
        if (ts.peek() == GEM || ts.peek() == RUTINE) {
            Dcl();
        } else if (ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR || ts.peek() == HVIS) {
            Stmt();
        } else error("Expected gem, rutine, sæt, gentag, kør eller hvis");
    }

    public void Dcl() {
        if (ts.peek() == GEM) {
            expect(GEM);
            Type();
            Value();
            expect(SOM);
            Id();
        }
        if (ts.peek() == RUTINE) {
            Fnc_dcl();
        } else error("Expected gem");
    }

    public void Type() {
        ArrayList<Token> tokens = ;
        boolean err = !peekAndExpectTokens(tokens);
        if (err) error("Expected tekst, heltal, decimaltal or boolsk");
    }

    public void Fnc_dcl() {
        if (ts.peek() == RUTINE) {
            expect(RUTINE);
            Id();
            expect(COLON);
            expect(NEWLINE);
            Stmts();
            return;
        } else error("Expected rutine");
    }

    public void Stmt() {
        if (ts.peek() == SET) {
            Assign();
            return;
        } else if (ts.peek() == GENTAG) {
            Loop();
            return;
        } else if (ts.peek() == KOR) {
            Func();
            return;
        } else if (ts.peek() == HVIS) {
            If();
            return;
        } else error("Expected sæt, gentag, kør or hvis");
        expect(NEWLINE);
    }

    public void Stmts() {
        if (ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR || ts.peek() == HVIS) {
            Stmt();
            Stmts();
        } else error("Expected sæt, gentag, kør or hvis");
    }

    public void Assign() {
        if (ts.peek() == SET) {
            expect(SET);
            Id();
            expect(TIL);
            Value();
        } else error("Expected sæt");
    }

    public void Loop() {
        if (ts.peek() == GENTAG) {
            expect(GENTAG);
            Func();
            Integer();
            expect(GANGE);
        } else error("Expected gentag");
    }

    public void Func() {
        if (ts.peek() == KOR) {
            expect(KOR);
            Id();
        } else error("Expected kør");
    }

    public void If() {
        if (ts.peek() == HVIS) {
            expect(HVIS);
            Expr();
            expect(COLON);
            expect(NEWLINE);
            Stmts();
        } else error("Expected hvis");
    }

    /**
     * EXPRESSION
     */
    public void Expr() {
        if (ts.peek() == LETTER) {
            Or_expr();
        } else error("Expected letter");
    }

    public void Or_expr() {
        And_expr();
        Or_expr2();
        /** TODO: Hvordan handler vi errors her **/

    }

    public void Or_expr2() {
        if (ts.peek() == ELLER) {
            expect(ELLER);
            And_expr();
            Or_expr2();

        } else error("FORVENTEDE ELLER");
    }

    public void And_expr() {
        Equality_expr();
        And_expr2();
        /** TODO: Hvordan handler vi errors her **/
        error("Expected og");
    }

    public void And_expr2() {
        if (ts.peek() == OG){
            expect(OG);
            Equality_expr();
            And_expr2();
        } else error("Expected og");
    }

    public void Equality_expr(){
        Rel_expr();
        Equality_expr2();
    }

    public void Equality_expr2(){
        if (ts.peek() == ER){
            expect(ER);
            Rel_expr();
        } else if (ts.peek() == IKKEER){
            expect(IKKEER);
            Rel_expr();
        } else error("Expected er or ikke er");
    }

    public void Rel_expr(){
        Sum_expr();
        if (ts.peek() == GREATER) {
            expect(GREATER);
            Sum_expr();
        } else if (ts.peek() == LESSER) {
            expect(LESSER);
            Sum_expr();
        }
        error("Expected > or <");
    }

    public void Sum_expr(){
        Product_expr();
        if (ts.peek() == PLUS) {
            expect(PLUS);
            Sum_expr();
        } else if (ts.peek() == MINUS) {
            expect(MINUS);
            Sum_expr();
        }
        error("Expected + or -");
    }

    public void Product_expr(){
        Not_expr();
        if (ts.peek() == TIMES) {
            expect(TIMES);
            Product_expr();
        } else if (ts.peek() == DIVIDE) {
            expect(DIVIDE);
            Product_expr();
        }
        error("Expected * or /");
    }

    public void Not_expr(){
        if (ts.peek() == IKKE) {
            expect(IKKE);
        } else error("Expected ikke");
        Factor();
    }

    public void Factor() {
        if (ts.peek() == LPAREN) {
            expect(LPAREN);
            Expr();
            expect(RPAREN);
        } else if (ts.peek() == DIGIT || ts.peek() == QUOTE || ts.peek() == BOOLSK) {
            Value();
        } else if (ts.peek() == LETTER) {
            Id();
        }
        error("Expected (, digit, quote, boolean or letter");
    }

    public void Value(){
        if (ts.peek() == QUOTE) {
            Parser_string();
        } else if (ts.peek() == DIGIT) {
            /** TODO: INTEGER ELLER FLOAT? **/
        } else if (ts.peek() == BOOLSK) {
            expect(BOOLSK);
        }
        error("Expected quote, digit or boolean");
    }


    public void Integer(){
        if (ts.peek() == MINUS) {
            expect(MINUS);
        }
        if (ts.peek() == DIGIT) {
            expect(DIGIT);
            Integer();
        }
    }
    public void Float(){
        if (ts.peek() == DIGIT){
            /** TODO: LAV FLOAT **/
        }
    }

    private boolean peekAndExpectTokens(ArrayList<TokenType> tokens) {
        for(TokenType token : tokens){
            if(ts.peek() == token){
                expect(token);
                return true;
            }
        }
        return false;
    }

    public void Parser_string(){
        if (ts.peek() == QUOTE) {
            expect(QUOTE);
        } else if (ts.peek() == LETTER) {
            Alphanumeric();
            Parser_string();
        }
    }

    public void Alphanumeric(){
        if (ts.peek() == LETTER){
            expect(LETTER);
        } else if (ts.peek() == DIGIT){
            expect(DIGIT);
        }
    }
    public void Id() {
        expect(LETTER);
        if (ts.peek() == LETTER || ts.peek() == DIGIT) {
            Alphanumeric();
        }
    }

    private void expect(TokenType type) {
        Token t = ts.advance();
        if (t.type != type) {
            throw new Error("Expected type "
                    + type
                    + " but received type "
                    + t.type);

        }
    }

    private void error(String message) {
        throw new Error(message);
    }

    /** TODO: Jeg har refactoret den her @AJ, er den stadig korrekt ifht det du forventede? **/
    private boolean peekAndExpectTokens(List<TokenType> tokenTypes) {
        for(TokenType tokenType : tokenTypes){
            if(ts.peek() == tokenType){
                expect(tokenType);
                return true;
            }
        }
        return false;
    }
}
