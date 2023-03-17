package ASTVisitor.Parser;

import ASTVisitor.ASTnodes.*;
import ASTVisitor.Lexer.CharStream;
import ASTVisitor.Lexer.Token;
import ASTVisitor.Lexer.TokenStream;
import ASTVisitor.Lexer.TokenType;

import java.util.ArrayList;

import static ASTVisitor.Lexer.TokenType.*;

public class ASTParser {

    private final TokenStream ts;

    public ASTParser(CharStream charStream) {
        this.ts = new TokenStream(charStream);
    }

    public AST prog() {
        ProgramNode progAST = new ProgramNode(new ArrayList<>());
        ArrayList<AST> linesList = lines();
        expect(EOF);
        if(linesList != null) progAST.getChild().addAll(linesList);
        return progAST;
    }

    public ArrayList<AST> lines() {
        ArrayList<AST> linesList = new ArrayList<>();
        if(ts.peek() == GEM || ts.peek() == RUTINE ||
                ts.peek() == SAET || ts.peek() == GENTAG ||
                ts.peek() == KOER || ts.peek() == HVIS) {
            AST line = line();
            ArrayList<AST> lines = lines();
            linesList.add(line);
            linesList.addAll(lines);
        } else if (ts.peek() == EOF) {
            // Do nothing (lambda-production)
        } else error("Forventede gem, rutine, sæt, gentag, kør eller hvis.");
        return linesList;
    }

    private AST line() {
        AST lineAST = null;
        if(ts.peek() == GEM || ts.peek() == RUTINE) {
            lineAST = dcl();
        } else lineAST = stmt();

        return lineAST;
    }

    private AST dcl() {
        AST dclAST = null;
        if(ts.peek() == GEM){
            expect(GEM);
            dclAST = varDcl();
        } else {
            expect(RUTINE);
            dclAST = funcDcl();
        }
        return dclAST;
    }

    private AST stmt() {
        AST stmtAST = null;
        if(ts.peek() == SAET) {
            expect(SAET);
            Token idToken = expect(ID);
            expect(TIL);
            AST value = value();
            stmtAST = new AssignNode(idToken.getVal(), value);
        } else if(ts.peek() == GENTAG) {
            expect(GENTAG);
            // func call
            Token funcCall = expect(ID);
            Token intToken = expect(HELTAL_LIT);
            expect(GANGE);
            stmtAST = new LoopNode(new FuncNode(funcCall.getVal()),
                    new HeltalLiteral(intToken.getVal()));
        } else if(ts.peek() == KOER) {
            expect(KOER);
            Token idToken = expect(ID);
            stmtAST = new FuncNode(idToken.getVal());
        } else if (ts.peek() == HVIS) {
            expect(HVIS);
            expr();
        } else error("Forventede kommando (sæt, gentag, kør eller hvis).");
        return stmtAST;
    }

    public AST expr() {
        AST expr = null;
        if (ts.peek() == ID || ts.peek() == HELTAL_LIT || ts.peek() == IKKE || ts.peek() == LPAREN
                || ts.peek() == TEKST_LIT || ts.peek() == DECIMALTAL_LIT || ts.peek() == BOOLSK_LIT) {
            expr = or_expr();
        } else error("Forventede boolsk udtryk.");
        return expr;
    }

    // TODO: Kigge på or_expr og resten af expression-delene
    public AST or_expr() {
        AST expr;
        AST andExpr = and_expr();
        AST orExpr = or_expr2();

        if(orExpr != null) expr = new BinaryComputing("eller", andExpr, orExpr);
        else expr = andExpr;
        
        return expr;
    }

    public AST or_expr2() {
        AST orExpr = null;
        if (ts.peek() == ELLER) {
            expect(ELLER);
            AST andExpr = and_expr();
            AST ors = or_expr2();
            if(ors != null) orExpr = new BinaryComputing("eller", andExpr, ors);
            else orExpr = andExpr;
        } else if (ts.peek() == RPAREN || ts.peek() == BLOCKSTART) {
            // produce nothing
        } else error("Forventede udtryk eller kolon. Fandt " + ts.advance());
        return orExpr;
    }

    public AST and_expr() {
        AST expr = null;
        equality_expr();
        and_expr2();

        return expr;
    }

    public void and_expr2() {
        if (ts.peek() == OG) {
            expect(OG);
            equality_expr();
            and_expr2();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == RPAREN) {
            // Do nothing (empty set)
        } else error("Forventede udtryk eller kolon. Fandt " + ts.advance());
    }

    public void equality_expr() {
        rel_expr();
        equality_expr2();
    }

    public void equality_expr2() {
        if (ts.peek() == ER) {
            expect(ER);
            rel_expr();
        } else if (ts.peek() == IKKE) {
            expect(IKKE);
            expect(ER);
            rel_expr();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == RPAREN) {
            // Do nothing, empty set
        } else error("FORVENTEDE er ELLER ikke er");
    }

    public void rel_expr() {
        sum_expr();
        rel_expr2();
    }

    public void rel_expr2() {
        if (ts.peek() == LESSER) {
            expect(LESSER);
            sum_expr();
        } else if (ts.peek() == GREATER) {
            expect(GREATER);
            sum_expr();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG
                || ts.peek() == RPAREN || ts.peek() == ER || ts.peek() == IKKE) {
            // Do nothing (empty set)
        } else error("FORVENTEDE < ELLER >");
    }

    public void sum_expr() {
        product_expr();
        sum_expr2();
    }

    public void sum_expr2() {
        if (ts.peek() == PLUS) {
            expect(PLUS);
            product_expr();
            sum_expr2();
        } else if (ts.peek() == MINUS) {
            expect(MINUS);
            product_expr();
            sum_expr2();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == ER || ts.peek() == IKKE
                || ts.peek() == LESSER || ts.peek() == GREATER || ts.peek() == RPAREN) {
            // Do nothing, empty set
        } else error("FORVENTEDE + ELLER -");
    }

    public void product_expr() {
        not_expr();
        product_expr2();
    }

    public void product_expr2() {
        if (ts.peek() == TIMES) {
            expect(TIMES);
            not_expr();
            product_expr2();
        } else if (ts.peek() == DIVIDE) {
            expect(DIVIDE);
            not_expr();
            product_expr2();
        } else if (ts.peek() == BLOCKSTART || ts.peek() == ELLER || ts.peek() == OG || ts.peek() == ER
                || ts.peek() == LESSER || ts.peek() == GREATER || ts.peek() == RPAREN
                || ts.peek() == PLUS || ts.peek() == MINUS) {
            // Do nothing (empty set)
        } else error("FORVENTEDE * ELLER /");
    }

    public void not_expr() {
        if (ts.peek() == IKKE) {
            expect(IKKE);
            Factor();
        } else Factor();

    }

    //TODO skriv ordentlig fejlbesked
    public void Factor() {
        if (ts.peek() == LPAREN) {
            expect(LPAREN);
            expr();
            expect(RPAREN);
        } else if (ts.peek() == HELTAL_LIT || ts.peek() == TEKST_LIT || ts.peek() == DECIMALTAL_LIT || ts.peek() == BOOLSK_LIT) {
            value();
        } else if (ts.peek() == ID) {
            expect(ID);
        } else error("FORVENTEDE (, heltal, decimaltal, tekst ELLER boolsk");
    }


    private AST varDcl(){
        AST dclAST = null;
        if(ts.peek() == TEKST_DCL){
            expect(TEKST_DCL);
            AST value = value();
            expect(SOM);
            Token idToken = expect(ID);
        } else if(ts.peek() == HELTAL_DCL){
            expect(HELTAL_DCL);
            AST value = value();
            expect(SOM);
            Token idToken = expect(ID);
        } else if(ts.peek() == DECIMALTAL_DCL){
            expect(DECIMALTAL_DCL);
            AST value = value();
            expect(SOM);
            expect(ID);
        } else if (ts.peek() == BOOLSK_DCL) {
            expect(BOOLSK_DCL);
            expect(BOOLSK_LIT);
            expect(SOM);
        } else error("Forventede type deklaration (tekst, heltal, decimaltal eller boolsk).");
        return dclAST;
    }

    private AST value() {
        AST valueAST = null;
        if (ts.peek() == TEKST_LIT) {
            Token tekstToken = expect(TEKST_LIT);
            valueAST = new TekstLiteral(tekstToken.getVal());
        } else if (ts.peek() == HELTAL_LIT) {
            Token heltalToken = expect(HELTAL_LIT);
            valueAST = new HeltalLiteral(heltalToken.getVal());
        } else if (ts.peek() == DECIMALTAL_LIT) {
            Token decimaltalToken = expect(DECIMALTAL_LIT);
            valueAST = new TekstLiteral(decimaltalToken.getVal());
        } else if (ts.peek() == BOOLSK_LIT) {
            Token boolskToken = expect(BOOLSK_LIT);
            valueAST = new TekstLiteral(boolskToken.getVal());
        }

        return valueAST;
    }

    private AST funcDcl(){
        return null;
    }

    private AST assStmt() {
        expect(ID);
        return null;
    }

    private Token expect(TokenType type) {
        Token token = ts.advance();
        if (token.getType() != type) {
            throw new Error("Forventede typen " +
                    token.getType() +
                    "men fik typen " +
                    type);
        }
        return token;
    }

    private void error(String message) {
        throw new Error(message);
    }
}