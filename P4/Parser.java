package P4Parser;

import java.util.ArrayList;

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
        if (ts.peek() == GEM || ts.peek() == RUTINE || ts.peek() == SÆT || ts.peek() == GENTAG || ts.peek() == KØR || ts.peek() == HVIS) {
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
        ArrayList<Token> tokens = [TEKST, HELTAL, DECIMALTAL, BOOLSK];
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
        }
    }

    public void Stmt(){
        if (ts.peek() == SÆT) {
            Assign();
        } else if (ts.peek() == GENTAG) {
            Loop();
        } else if (ts.peek() == KØR) {
            Func();
        } else if (ts.peek() == HVIS) {
            If();
        }
        expect(NEWLINE);
    }

    public void Stmts(){
        if (ts.peek() == SÆT || ts.peek() == GENTAG || ts.peek() == KØR ||ts.peek() == HVIS){
            Stmt();
            Stmts();
        }
    }

    public void Assign(){
        if (ts.peek() == SÆT) {
            expect(SÆT);
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
        if (ts.peek() == KØR) {
            expect(KØR);
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
        if(ts.peek() == OG){
            expect(OG);
            And_expr();
        }
    }

    public void Equality_expr(){
        Rel_expr();
        if (ts.peek() == ER) {
            expect(ER);
            Rel_expr();
        } else if (ts.peek() == IKKEER){
            expect(IKKEER);
            Equality_expr();
        }
    }

    public void Rel_expr(){
    }

    public void Sum_expr(){
    }

    public void Product_expr(){
    }

    public void Not_expr(){
    }

    public void Factor(){
    }

    public void Value(){
    }

    public void Integer(){
    }

    public void Float(){
    }

    public void parser_string(){
    }

    public void Alphanumeric(){
    }
    public void Letter(){
    }
    public void Digit(){
    }
    public void Id(){
    }
    public void Boolean(){
    }
    public void Indent(){
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

    private boolean peekAndExpectTokens(ArrayList<Token> tokens) {
        for(Token token : tokens){
            if(ts.peek() == token){
                expect(token);
                return true;
            }
        }
        return false;
    }
}
