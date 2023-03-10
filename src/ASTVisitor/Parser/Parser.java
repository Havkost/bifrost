package ASTVisitor.Parser;

import ASTVisitor.Lexer.Token;
import ASTVisitor.Lexer.TokenStream;
import static ASTVisitor.Lexer.TokenType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ASTVisitor.Lexer.*;

public class Parser {

    // TODO: LAV EMPTY SET PRODUCTIONS
    // TODO: LAV VALUE (HVORDAN VÆRDIER FRA STRING TIL INT)
    private TokenStream ts;

    public Parser(CharStream s) {
        ts = new TokenStream(s);
    }

    public void Program() {
        if (ts.peek() == EOF) return;
        Lines();
    }

    public void Lines() {
        if (ts.peek() == GEM || ts.peek() == RUTINE || ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR || ts.peek() == HVIS) {
            Line();
            expect(NEWLINE);
            Lines();
        } else if (ts.peek() == EOF) {
            // do nothing (empty set)
        } else error("Forventede gem, rutine, sæt, gentag, kør, eller hvis");
    }

    public void Line() {
        if (ts.peek() == GEM || ts.peek() == RUTINE) {
            Dcl();
        } else if (ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR || ts.peek() == HVIS) {
            Stmt();
        }
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
        } else error("Expected gem");
    }

    public void Fnc_dcl() {
        expect(RUTINE);
        expect(ID);
        expect(BLOCKSTART);
        expect(NEWLINE);
        Stmts();
        expect(DOT);
    }

    public void Stmt() {
        if (ts.peek() == SET) {
            Assign();
        } else if (ts.peek() == GENTAG) {
            Loop();
        } else if (ts.peek() == KOR) {
            Func();
        } else if (ts.peek() == HVIS) {
            If();
        } else error("Expected sæt, gentag, kør or hvis");
        expect(NEWLINE); //TODO Er denne nødvendig?
    }

    public void Stmts() {
        if (ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR || ts.peek() == HVIS) {
            Stmt();
            Stmts();
        } else if (ts.peek() == NEWLINE) {
            // do nothing (empty set)
        } else error("Expected sæt, gentag, kør or hvis");
    }

    public void Assign() {
        expect(SET);
        expect(ID);
        expect(TIL);
        Value();
    }

    public void Loop() {
        expect(GENTAG);
        Func();
        expect(INTEGER);
        expect(GANGE);
    }

    public void Func() {
        expect(KOR);
        expect(ID);
    }

    public void If() {
        expect(HVIS);
        Expr();
        expect(COLON);
        expect(NEWLINE);
        Stmts();
        expect(DOT);
    }

    /**
     * EXPRESSION
     */
    public void Expr() {
        if (ts.peek() == ID || ts.peek() == INTEGER || ts.peek() == IKKE || ts.peek() == LPAREN
                || ts.peek() == STRING ||ts.peek() == FLOAT || ts.peek() == BOOLEAN ) {
            Or_expr();
        } else error("Expected letter");
    }

    public void Or_expr(){
        And_expr();
        Or_expr2();
    }

    public void Or_expr2() {
        if (ts.peek() == ELLER) {
            expect(ELLER);
            And_expr();
            Or_expr2();
        } else if (ts.peek() == RPAREN || ts.peek() == COLON) {
            // produce nothing
        } else error("FORVENTEDE ELLER");
    }

    public void And_expr() {
        Equality_expr();
        And_expr2();
    }

    public void And_expr2() {
        if (ts.peek() == OG){
            expect(OG);
            Equality_expr();
            And_expr2();
        } else if(ts.peek() == COLON || ts.peek() == ELLER || ts.peek() == RPAREN){
            // Do nothing (empty set)
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
        } else if(ts.peek() == COLON || ts.peek() == ELLER || ts.peek() == OG|| ts.peek() == RPAREN){
          // Do nothing, empty set
        } else error("Expected er or ikke er");
    }

    public void Rel_expr(){
        Sum_expr();
        Rel_expr2();
    }

    public void Rel_expr2() {
        if (ts.peek() == LESSER){
            expect(LESSER);
            Sum_expr();
        } else if (ts.peek() == GREATER){
            expect(GREATER);
            Sum_expr();
        } else if (ts.peek() == COLON || ts.peek() == ELLER || ts.peek() == OG
                || ts.peek() == RPAREN || ts.peek() == ER || ts.peek() == IKKE) {
            // Do nothing (empty set)
        } else error("FORVENTEDE < ELLER >");
    }

    public void Sum_expr(){
        Product_expr();
        Sum_expr2();
    }

    public void Sum_expr2(){
        if (ts.peek() == PLUS) {
            expect(PLUS);
            Product_expr();
            Sum_expr2();
        } else if (ts.peek() == MINUS) {
            expect(MINUS);
            Product_expr();
            Sum_expr2();
        } else if (ts.peek() == COLON || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == ER || ts.peek() == IKKE
                || ts.peek() == LESSER || ts.peek() == GREATER || ts.peek() == RPAREN) {
            // Do nothing, empty set
        } else error("FORVENTEDE + ELLER -");
    }

    public void Product_expr() {
        Not_expr();
        Product_expr2();
    }

    public void Product_expr2(){
        if (ts.peek() == TIMES) {
            expect(TIMES);
            Not_expr();
            Product_expr2();
        } else if (ts.peek() == DIVIDE) {
            expect(DIVIDE);
            Not_expr();
            Product_expr2();
        } else if (ts.peek() == COLON || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == ER
                || ts.peek() == LESSER || ts.peek() == GREATER || ts.peek() == RPAREN
                || ts.peek() == PLUS || ts.peek() == MINUS) {
            // Do nothing (empty set)
        } else error("FORVENTEDE * ELLER /");
    }

    public void Not_expr(){
        if (ts.peek() == IKKE) {
            expect(IKKE);
            Factor();
        } else Factor();

    }

    //TODO skriv ordentlig fejlbesked
    public void Factor() {
        if (ts.peek() == LPAREN){
            expect(LPAREN);
            Expr();
            expect(RPAREN);
        } else if (ts.peek() == INTEGER || ts.peek() == STRING || ts.peek() == FLOAT || ts.peek() == BOOLEAN) {
            Value();
        } else if (ts.peek() == ID) {
            expect(ID);
        } else error("FORVENTEDE bla bla bla");
    }

    public void Value(){
        if (ts.peek() == STRING) {
            expect(STRING);
        } else if (ts.peek() == INTEGER) {
            expect(INTEGER);
        } else if (ts.peek() == FLOAT) {
            expect(FLOAT);
        } else if (ts.peek() == BOOLEAN) {
            expect(BOOLEAN);
        } else error("FORVENTEDE TEKST, HELTAL, DECIMALTAL, BOOLSK VÆRDIER");
    }

    public void Type() {
        if (ts.peek() == TEKST) {
            expect(TEKST);
        } else if (ts.peek() == HELTAL) {
            expect(HELTAL);
        } else if (ts.peek() == DECIMALTAL) {
            expect(DECIMALTAL);
        } else if (ts.peek() == BOOLSK) {
            expect(BOOLSK);
        } else error("Type ikke genkendt");
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

