package ASTVisitor;
import java.util.ArrayList;
import java.util.List;

import static ASTVisitor.TokenType.*;

public class Parser {

    /* TODO: Lav Tokenstream, CharStream, ScannerCode, Token classes */

    private TokenStream ts;

    public Parser(CharStream s) {
        ts = new TokenStream(s);
    }

    public void Program() {
        if(ts.peek() == EOF) return;
        Line();
        Program();
    }

    public void Line(){
        if (ts.peek() == GEM || ts.peek() == RUTINE || ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR || ts.peek() == HVIS) {
            Dcl();
            Stmt();
            return;
        }
        error("Expected gem, rutine, sæt, gentag, kør eller hvis");
    }

    public void Dcl(){
        if (ts.peek() == GEM){
            expect(GEM);
            Type();
            Value();
            expect(SOM);
            Id();
            return;
        }
        if(ts.peek() == RUTINE){
            Fnc_dcl();
            return;
        }
        error("Expected gem");
    }

    public void Type(){
        ArrayList<TokenType> tokens = new ArrayList<>(List.of(TEKST, HELTAL, DECIMALTAL, BOOLSK));
        boolean err = !peekAndExpectTokens(tokens);
        if(err) error("Expected tekst, heltal, decimaltal or boolsk");
    }

    public void Fnc_dcl(){
        if (ts.peek() == RUTINE) {
            expect(RUTINE);
            Id();
            expect(COLON);
            expect(NEWLINE);
            Stmts();
            return;
        }
        error("Expected rutine");
    }

    public void Stmt(){
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

    public void Stmts(){
        if (ts.peek() == SET || ts.peek() == GENTAG || ts.peek() == KOR ||ts.peek() == HVIS){
            Stmt();
            Stmts();
        }
    }

    public void Assign(){
        if (ts.peek() == SET) {
            expect(SET);
            Id();
            expect(TIL);
            Value();
        }
    }

    public void Loop(){
        if (ts.peek() == GENTAG) {
            expect(GENTAG);
            Func();
            Integer();
            expect(GANGE);
        }
    }

    public void Func(){
        if (ts.peek() == KOR) {
            expect(KOR);
            Id();
        }
    }

    public void If(){
        if (ts.peek() == HVIS) {
            expect(HVIS);
            Expr();
            expect(COLON);
            expect(NEWLINE);
            Stmts();
        }
    }

    /** EXPRESSION */
    public void Expr(){
        if (ts.peek() == LETTER) {
            Or_expr();
        }
    }

    public void Or_expr(){
        And_expr();
        if(ts.peek() == ELLER) {
            expect(ELLER);
            Or_expr();
        }
    }

    public void And_expr(){
        Equality_expr();
        if(ts.peek() == OG) {
            expect(OG);
            And_expr();
        }
    }

    public void Equality_expr(){
        Rel_expr();
        if (ts.peek() == ER) {
            expect(ER);
            Rel_expr();
        } else if (ts.peek() == IKKEER) {
            expect(IKKEER);
            Equality_expr();
        }
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
    }

    public void Not_expr(){
        if (ts.peek() == IKKE) {
            expect(IKKE);
        }
        Factor();
    }

    public void Factor() {
        if (ts.peek() == LPAREN) {
            expect(LPAREN);
            Expr();
            expect(RPAREN);
        }
        if (ts.peek() == DIGIT || ts.peek() == QUOTE || ts.peek() == BOOLSK) {
            Value();
        }
        if (ts.peek() == LETTER) {
            Id();
        }
    }

    public void Value(){
        if (ts.peek() == QUOTE) {
            Parser_string();
        } else if (ts.peek() == DIGIT) {
            /** TODO: INTEGER ELLER FLOAT? **/
        } else if (ts.peek() == BOOLSK) {
            expect(BOOLSK);
        }
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
    private boolean peekAndExpectTokens(ArrayList<TokenType> tokens) {
        for(TokenType token : tokens){
            if(ts.peek() == token){
                expect(token);
                return true;
            }
        }
        return false;
    }
}
